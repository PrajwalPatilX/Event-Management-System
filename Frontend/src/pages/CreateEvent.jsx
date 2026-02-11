import { useState } from "react";
import { useNavigate } from "react-router-dom";
import API from "../api";

export default function CreateEvent() {
  const [form, setForm] = useState({
    title: "",
    description: "",
    startTime: "",
    endTime:"",
    organizationId:"",
  });

  const navigate = useNavigate();

  function handleChange(e) {
    setForm({ ...form, [e.target.name]: e.target.value });
  }

  async function handleSubmit(e) {
    e.preventDefault();

    try {
      await API.post("/events", form);
      alert("Event created.");
      navigate("/dashboard");
    } catch {
      alert("Backend said no. Shocking.");
    }
  }

  return (
    <div>
      <h2>Create Event</h2>

      <form onSubmit={handleSubmit}>
        <input name="title" placeholder="Event Name" onChange={handleChange} />
        <br />

        <textarea
          name="description"
          placeholder="Description"
          onChange={handleChange}
        />
        <br />

        <input type="date" name="startTime" onChange={handleChange} />
        <br />

        <input type="date" name="endTime" onChange={handleChange} />
        <br />

        <input name="organizationId" placeholder="OrganizationId" onChange={handleChange} />
        <br />

        <button>Create Event</button>
      </form>
    </div>
  );
}
