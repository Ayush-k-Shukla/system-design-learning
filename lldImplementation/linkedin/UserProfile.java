package linkedin;

import java.util.List;

public class UserProfile {
    private final String profileImage;
    private final String headline;
    private final String summary;
    private final List<Skill> skills;
    private final List<Education> educations;
    private final List<Experience> experiences;

    private UserProfile(Builder builder){
        this.headline = builder.headline;
        this.profileImage = builder.profileImage;
        this.skills =  builder.skills!=null ? List.copyOf(builder.skills):List.of();
        this.summary = builder.summary;
        this.educations =  builder.educations!=null ? List.copyOf(builder.educations):List.of();
        this.experiences =  builder.experiences!=null ? List.copyOf(builder.experiences):List.of();
    }

    public List<Education> getEducations() {
        return educations;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public static class Builder {
        private String profileImage;
        private String headline;
        private String summary;
        private List<Skill> skills;
        private List<Education> educations;
        private List<Experience> experiences;

        public Builder profileImage(String profileImage){
            this.profileImage = profileImage;
            return this;
        }

        public Builder headline(String headline){
            this.headline = headline;
            return this;
        }

        public Builder summary(String summary){
            this.summary = summary;
            return this;
        }

        public Builder skills(List<Skill> skills){
            this.skills = skills;
            return this;
        }

        public Builder experiences(List<Experience> experiences){
            this.experiences = experiences;
            return this;
        }

        public Builder educations(List<Education> educations){
            this.educations = educations;
            return this;
        }

        public UserProfile build(){
            return new UserProfile(this);
        }
    }
}