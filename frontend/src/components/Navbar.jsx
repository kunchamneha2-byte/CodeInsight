import React from 'react';
import { 
  Code2, 
  LayoutDashboard, 
  Terminal, 
  LineChart, 
  History, 
  RotateCcw,
  Sparkles
} from 'lucide-react';

export default function Navbar({ activeTab, setActiveTab, onResetData }) {
  return (
    <header className="navbar">
      <div className="nav-brand" onClick={() => setActiveTab('dashboard')}>
        <div className="brand-icon">
          <Code2 size={22} strokeWidth={2.5} />
        </div>
        <div>
          <div className="brand-title">CodeInsight</div>
          <div className="brand-subtitle">AI-Powered Coding Assistant</div>
        </div>
      </div>

      <nav className="nav-tabs">
        <button
          className={`nav-tab-btn ${activeTab === 'dashboard' ? 'active' : ''}`}
          onClick={() => setActiveTab('dashboard')}
        >
          <LayoutDashboard size={17} />
          <span>Dashboard</span>
        </button>

        <button
          className={`nav-tab-btn ${activeTab === 'practice' ? 'active' : ''}`}
          onClick={() => setActiveTab('practice')}
        >
          <Terminal size={17} />
          <span>Practice Lab</span>
        </button>

        <button
          className={`nav-tab-btn ${activeTab === 'analysis' ? 'active' : ''}`}
          onClick={() => setActiveTab('analysis')}
        >
          <LineChart size={17} />
          <span>Weak-Topic Insights</span>
        </button>

        <button
          className={`nav-tab-btn ${activeTab === 'history' ? 'active' : ''}`}
          onClick={() => setActiveTab('history')}
        >
          <History size={17} />
          <span>Attempt History</span>
        </button>
      </nav>

      <div className="nav-actions">
        <div className="status-badge" title="Connected to Spring Boot & MySQL">
          <span className="status-dot"></span>
          <span>Spring Boot + MySQL</span>
        </div>

        <button
          className="btn-secondary"
          style={{ fontSize: '0.78rem', padding: '0.4rem 0.75rem' }}
          onClick={onResetData}
          title="Reset practice attempts for testing"
        >
          <RotateCcw size={14} />
          <span>Reset Attempts</span>
        </button>
      </div>
    </header>
  );
}
