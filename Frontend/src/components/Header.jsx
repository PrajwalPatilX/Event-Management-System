import React from "react";
import { useNavigate } from "react-router-dom";
import API from "../api";

function Header() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");


  const handleLogout = async () => {
    try {
      await API.post("/auth/logout");
    } catch (error) {
      console.log("Logout API failed (safe to ignore)");
    }

    localStorage.removeItem("token");
    navigate("/");
  };

  return (
    <header
    >
      <h2 style={{ margin: 0 }}>Event Platform</h2>

      { token &&(
      <button className="btn btn-primary"
        onClick={handleLogout}
      >
        Logout
      </button>)}
    </header>
  );
}

export default Header;
