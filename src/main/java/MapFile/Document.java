package main.java.MapFile;

import java.util.Date;

import com.google.api.client.http.GenericUrl;

public class Document {
	
      int id;
      int folderId;
      String display_name;
      String filename;
      String content_type;
      String url;
      int size;
      Date created_at;
      Date updated_at;
      String unlock_at;
      boolean locked;
      boolean hidden;
      String lock_at;
      boolean hidden_for_user;
      GenericUrl thumbnail_url;
      Date modified_at;
      String mime_class;
      String media_entry_id;
      boolean locked_for_user;
      String preview_url;
}
