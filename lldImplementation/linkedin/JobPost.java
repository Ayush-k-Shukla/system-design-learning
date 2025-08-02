package linkedin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JobPost {
    private final String title;
    private final String description;
    private final String location;
    private final String requirement;
    private final Map<String, User> applicants;


    private JobPost(Builder builder){
        this.title = builder.title;
        this.description = builder.description;
        this.location = builder.location;
        this.requirement = builder.requirement;
        this.applicants = builder.applicants != null ? Map.copyOf(builder.applicants) : new HashMap<>();
    }

    public String getDescription() {
        return description;
    }

    public List<User> getApplicant() {
        return applicants.values().stream().toList();
    }

    public String getRequirement() {
        return requirement;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public void addApplicant(User user){
        if(!applicants.containsKey(user.getId())){
            applicants.put(user.getId(), user);
        }
    }

    public static class Builder {
        private String title;
        private String description;
        private String location;
        private String requirement;
        private Map<String, User> applicants;

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder description(String description){
            this.description = description;
            return this;
        }

        public Builder location(String location){
            this.location = location;
            return this;
        }

        public Builder requirement(String requirement){
            this.requirement  =requirement;
            return this;
        }

        public Builder applicants(Map<String, User> applicants){
            this.applicants = applicants;
            return this;
        }

        public JobPost build(){
            if(this.title == null || this.requirement == null){
                throw new IllegalArgumentException("title and requirements are required");
            }
            return new JobPost(this);
        }
    }

}
