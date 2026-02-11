import { useEffect, useState } from "react";
import API from "../api";
import { Link, useNavigate } from "react-router-dom";

export default function Dashboard() {
  const [profile, setProfile] = useState(null);
  const [events, setEvents] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    API.get("/user/me").then((res) => setProfile(res.data));
    API.get("/events/published").then((res) => setEvents(res.data));
  }, []);

  return (
    <div>
      <h1>Dashboard</h1>

      {profile && (
        <div>
          <h3>My Profile</h3>
          <p>Name: {profile.fullName}</p>
          <p>Email: {profile.email}</p>
          <p>Role: {profile.role}</p>
        </div>
      )}

      <h3>Published Events</h3>
      {events.map((e) => (
        <div key={e.id}>
           Event Id : <strong>{e.id}</strong><br />
           Event Name : <strong>{e.title}</strong><br />
           Description : <strong>{e.description}</strong><br />
           Status : <strong>{e.status}</strong><br />
           Start On : <strong>{e.startTime}</strong><br />
           End On : <strong>{e.endTime}</strong>
        </div>
      ))}
      <button onClick={() => navigate("/create-event")}>
        Create Event
      </button>
      <button onClick={() => navigate("/manage-events")}>
        Manage Event
      </button>
      <button onClick={() => navigate("/admin/users")}>
        Admin: Manage Users
      </button>
          <button onClick={() => navigate("/checkin")}>
        Participant Check-In
      </button>

      <button onClick={() => navigate("/attendance")}>
        View Attendance (Organizer)
      </button>
      <button onClick={() => navigate("/announcements/create")}>
        Create Announcement
      </button>

      <button onClick={() => navigate("/announcements/view")}>
        View Announcements
      </button>
      <button onClick={() => navigate("/teams/create")}>
        Create Team
      </button>

      <button onClick={() => navigate("/teams/join")}>
        Join Team
      </button>

      <button onClick={() => navigate("/organizations/create")}>
        Create Organization
      </button>

      <button
        onClick={() => navigate("/organizations/approved")}
      >
        View Approved Orgs
      </button>

      <button onClick={() => navigate("/admin/organizations")}>
        Admin: Approve Orgs
      </button>
    </div>
  );
}