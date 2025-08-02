package linkedin;

import java.util.Arrays;
import java.util.List;

import linkedin.Types.UserType;

public class LinkedinServiceDemo {
        public static void run() {
                LinkedinService service = LinkedinService.getInstance();

                UserProfile profile1 = new UserProfile.Builder()
                                .profileImage("img1.png")
                                .summary("Experienced Java Developer")
                                .headline("Engineer")
                                .educations(Arrays.asList(new Education("Delhi", "IIT", "2010", "2014")))
                                .experiences(Arrays.asList(new Experience("Bangalore", "SDE", "2014", "2018",


                                                "Worked on backend")))
                                .skills(Arrays.asList(new Skill("Java"), new Skill("System Design")))
                                .build();

                UserProfile profile2 = new UserProfile.Builder()
                                .profileImage("img1.png")
                                .summary("Experienced Java Developer")
                                .headline("Engineer2")
                                .educations(Arrays.asList(new Education("Delhi", "IIT", "2010", "2014")))
                                .experiences(Arrays.asList(new Experience("Bangalore", "SDE", "2014", "2018",
                                                "Worked on backend")))
                                .skills(Arrays.asList(new Skill("Java"), new Skill("System Design")))
                                .build();

                // Create Users
                service.createUser("Ayush", "ayush@email.com", "pass123", UserType.USER, profile1);
                service.createUser("Shukla", "shukla@email.com", "pass456", UserType.EMPLOYER, profile2);

                // Login Users
                User user1 = service.loginUser("ayush@email.com", "pass123");
                User user2 = service.loginUser("shukla@email.com", "pass456");
                System.out.println("Login Test: " + (user1 != null && user2 != null));

                // Send Connection Request
                service.sendConnectionRequest(user2, user1);
                System.out.println("Connection Request Sent");

                // Accept Connection Request
                service.acceptConnectionRequest(user2, user1);
                System.out.println("Connection Request Accepted");

                System.out.println("User 1 connection: " + user1.getConnections().size());


                // --- Job Post Functionality Tests ---

                JobPost post = new JobPost.Builder()
                        .title("Work on backend systems")
                        .location("BLR")
                        .description("backend devb")
                        .requirement("Java, c++")
                        .build();
                service.addJobPost(user2, post);


                List<JobPost> allJobs = service.searchJobs("backend");
                System.out.println("All Job Posts:");
                for (JobPost jp : allJobs) {
                        System.out.println("- " + jp.getTitle() + " at " + jp.getLocation());
                }


                service.applyJob(user1, allJobs.get(0));
                System.out.println("User1 applied to job: " + allJobs.get(0).getApplicant().size());



                // Send Message
                service.sendMessage(user1, user2, "Hello, let's connect!");
                System.out.println("Message Sent");

                // Search User
                List<User> found = service.searchUser("Ayush");
                System.out.println("Search Test: Found " + found.size() + " user(s)");

                // Print Connections
                System.out.println(
                                "User1 Connections: " + user1.getName() + " has " + user1.getConnections().size()
                                                + " connection(s)");
                System.out.println(
                                "User2 Connections: " + user2.getName() + " has " + user2.getConnections().size()
                                                + " connection(s)");

                // Print Messages
                System.out.println("User1 Messages: " + user1.getMessages().size());
                System.out.println("User2 Messages: " + user2.getMessages().size());

                // Send Connection Request again
                service.sendConnectionRequest(user2, user1);
                System.out.println("Connection Request Sent");

                // Reject Connection Request
                service.removeConnectionRequest(user2, user1);
                System.out.println("Connection Request Declined");

                System.out.println("User 1 connection: " + user1.getConnections().size());
        }
}
