package linkedin;

import java.util.List;
import java.util.UUID;

public class UserProfile {
    private String profileImage;
    private String headline;
    private String summary;
    private List<Skill> skills;
    private List<Education> educations;
    private List<Experience> experiences;

    public UserProfile(String profileImage, String headline, String summary, List<Skill> skills, List<Education> educations, List<Experience> experiences){
        this.headline = headline;
        this.profileImage = profileImage;
        this.skills = skills;
        this.educations = educations;
        this.experiences = experiences;
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
}