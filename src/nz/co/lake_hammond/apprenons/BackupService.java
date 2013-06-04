package nz.co.lake_hammond.apprenons;

import nz.co.lake_hammond.apprenons.sql.TraductionDatabase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

public class BackupService extends IntentService {
	public BackupService() {
		super("ApprenonsBackupThread");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		TraductionDatabase database = new TraductionDatabase(BackupService.this);
		try {
			showNotification(R.string.backup_notification_text_creating);
			String payload = database.getAllAsJson().toString(2);
			
			showNotification(R.string.backup_notification_text_sending);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://www.lake-hammond.co.nz:8081/upload");
			post.setEntity(new StringEntity(payload));
			HttpResponse response = client.execute(post);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode >= 200 && statusCode < 300)
				Toast.makeText(BackupService.this, R.string.backup_complete, Toast.LENGTH_LONG).show();
			else
				Toast.makeText(BackupService.this, R.string.backup_failed, Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			Toast.makeText(BackupService.this, R.string.backup_failed, Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		
		hideNotification();
	}
	
	void showNotification(int contentTextResId) {
		Builder builder = new NotificationCompat.Builder(BackupService.this)
			.setContentTitle(BackupService.this.getString(R.string.backup_notification_title))
			.setContentText(BackupService.this.getString(contentTextResId))
			.setOngoing(true)
			.setSmallIcon(R.drawable.ic_stat_backup)
			.setProgress(0, 0, true);
		
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE))
			.notify(1,builder.build());
	}
	
	void hideNotification() {
		((NotificationManager)getSystemService(NOTIFICATION_SERVICE))
			.cancel(1);
	}
}
