package main.java.MapFile;


import java.util.Date;
import java.util.List;

import com.google.api.client.http.GenericUrl;

public class Students {
	int id;
    String body;
    String url;
    float grade;
    float score;
    Date submittedAt;
    int assignmentId;
    int userId;
    String submissionType;
    String workflowState;
    boolean grade_matches_current_submission;
    Date gradedAt;
    int graderId;
    int attempt;
    boolean excused;
    boolean late;
    String preview_url;
    List<Document> attachments;
	
}
