package com.project.backend.service.team;

import com.project.backend.dto.registration.request.CreateTeamRequest;
import com.project.backend.dto.registration.request.JoinTeamRequest;
import com.project.backend.dto.team.response.TeamResponse;
import com.project.backend.entity.*;
import com.project.backend.repository.EventRepository;
import com.project.backend.repository.TeamMemberRepository;
import com.project.backend.repository.TeamRepository;
import com.project.backend.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository memberRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final JavaMailSender mailSender;

    // Create Team
    public TeamResponse create(CreateTeamRequest request, String email) {

        User leader = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        Team team = new Team();

        team.setName(request.getName());
        team.setEvent(event);
        team.setLeader(leader);
        String code = UUID.randomUUID().toString().substring(0, 6);
        team.setJoinCode(code);

        Team saved = teamRepository.save(team);

        TeamMember member = new TeamMember();
        member.setTeam(team);
        member.setUser(leader);
        member.setRole(TeamMemberRole.LEADER);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(leader.getEmail());
            helper.setSubject("Joininig team Code for your team"+request.getName());
            helper.setText("""
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="UTF-8">
            <title>Team Join Code</title>
        </head>
        <body style="margin:0; padding:0; background-color:#f4f6f9; font-family:Arial, sans-serif;">
        
            <table align="center" width="100%%" cellpadding="0" cellspacing="0" 
                   style="max-width:600px; margin:40px auto; background:#ffffff; border-radius:10px; overflow:hidden; box-shadow:0 4px 10px rgba(0,0,0,0.08);">

                <!-- Header -->
                <tr>
                    <td style="background:#1e293b; padding:20px; text-align:center; color:white;">
                        <h2 style="margin:0;">%s</h2>
                        <p style="margin:5px 0 0; font-size:14px;">Team Invitation</p>
                    </td>
                </tr>

                <!-- Content -->
                <tr>
                    <td style="padding:30px;">
                        <p style="font-size:16px;">Hi <strong>%s</strong>,</p>

                        <p style="font-size:15px; line-height:1.6;">
                            You have been invited to join the team 
                            <strong>%s</strong> for the event 
                            <strong>%s</strong>.
                        </p>

                        <p style="margin-top:20px; font-size:15px;">
                            Use the following join code to become a member:
                        </p>

                        <!-- Join Code Box -->
                        <div style="margin:25px 0; text-align:center;">
                            <span style="display:inline-block; padding:15px 30px; 
                                         font-size:22px; letter-spacing:3px; 
                                         background:#2563eb; color:#ffffff; 
                                         border-radius:8px; font-weight:bold;">
                                %s
                            </span>
                        </div>

                        <p style="font-size:14px; color:#555;">
                            Enter this code inside the platform to join the team.
                        </p>

                        <p style="margin-top:30px; font-size:14px; color:#888;">
                            If you did not expect this invitation, please ignore this email.
                        </p>
                    </td>
                </tr>

                <!-- Footer -->
                <tr>
                    <td style="background:#f1f5f9; padding:15px; text-align:center; font-size:12px; color:#777;">
                        © %d Your Event Platform. All rights reserved.
                    </td>
                </tr>

            </table>
        </body>
        </html>
        """.formatted(
                        event.getTitle(),
                        leader.getFullName(),
                        request.getName(),
                        event.getTitle(),
                        code,
                        java.time.Year.now().getValue())

, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }

        memberRepository.save(member);


        

        return TeamResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .joinCode(saved.getJoinCode())
                .eventId(event.getId())
                .build();
    }

    public void join(JoinTeamRequest request, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        Team team = teamRepository
                .findByJoinCode(request.getJoinCode())
                .orElseThrow();

        if (memberRepository.existsByTeamIdAndUserId(
                team.getId(), user.getId())) {
            throw new RuntimeException("Already joined");
        }

        TeamMember member = new TeamMember();

        member.setTeam(team);
        member.setUser(user);
        member.setRole(TeamMemberRole.MEMBER);

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Successfully Joined The Team"+team.getName());
            helper.setText("Congratulations On participating in the event.",true);
            mailSender.send(message);
        } catch (RuntimeException | MessagingException e) {
            throw new RuntimeException("Failed to send email");
        }
        memberRepository.save(member);
    }
}