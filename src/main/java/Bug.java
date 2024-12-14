public class Bug {
    private int id;
    private String description;
    private String examples;
    private String status;
    private String issueKey;

    public Bug(int id, String description, String examples, String status, String issueKey){
        this.description = description;
        this.id = id;
        this.examples = examples;
        this.status = status;
        this.issueKey = issueKey;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getExamples() {
        return examples;
    }

    public String getIssueKey() {
        return  issueKey;
    }

    public String getStatus() {
        return status;
    }

}
