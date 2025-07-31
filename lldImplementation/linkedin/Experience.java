package linkedin;

import java.util.Date;

public class Experience {
    private String location;
    private String role;
    private String startDate;
    private String endDate;
    private String summary;

    public Experience(String location, String role, String startDate, String endDate, String summary){
        this.role = role;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.summary = summary;
    }
}
