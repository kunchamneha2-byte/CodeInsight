import React, { useState, useEffect } from 'react';
import Navbar from './components/Navbar';
import Dashboard from './components/Dashboard';
import PracticeView from './components/PracticeView';
import WeakTopicAnalysis from './components/WeakTopicAnalysis';
import HistoryView from './components/HistoryView';
import { api } from './services/api';

export default function App() {
  const [activeTab, setActiveTab] = useState('dashboard');
  const [dashboardData, setDashboardData] = useState(null);
  const [selectedQuestionId, setSelectedQuestionId] = useState(null);
  const [selectedTopicSlug, setSelectedTopicSlug] = useState('ALL');

  useEffect(() => {
    loadDashboardData();
  }, []);

  const loadDashboardData = async () => {
    try {
      const data = await api.getDashboardSummary();
      setDashboardData(data);
    } catch (err) {
      console.error('Failed to load dashboard data:', err);
    }
  };

  const handleSelectTopic = (slug) => {
    setSelectedTopicSlug(slug);
    setSelectedQuestionId(null);
    setActiveTab('practice');
  };

  const handleSelectQuestion = (questionId) => {
    setSelectedQuestionId(questionId);
    setActiveTab('practice');
  };

  const handleResetData = async () => {
    if (window.confirm('Reset all submission history and practice progress in the MySQL database?')) {
      try {
        await api.resetProgress();
        await loadDashboardData();
        setSelectedQuestionId(null);
        setSelectedTopicSlug('ALL');
        alert('All previous submissions reset successfully. You now have a fresh practice slate!');
      } catch (err) {
        alert('Failed to reset: ' + err.message);
      }
    }
  };

  const handleQuestionCompleted = () => {
    loadDashboardData();
  };

  return (
    <div className="app-container">
      <Navbar 
        activeTab={activeTab} 
        setActiveTab={setActiveTab} 
        onResetData={handleResetData}
      />

      <main className="main-content">
        {activeTab === 'dashboard' && (
          <Dashboard
            dashboardData={dashboardData}
            onSelectTopic={handleSelectTopic}
            onSelectQuestion={handleSelectQuestion}
            onGoToPractice={() => {
              setSelectedTopicSlug('ALL');
              setSelectedQuestionId(null);
              setActiveTab('practice');
            }}
            onGoToAnalysis={() => setActiveTab('analysis')}
          />
        )}

        {activeTab === 'practice' && (
          <PracticeView
            selectedQuestionId={selectedQuestionId}
            initialTopicSlug={selectedTopicSlug}
            onQuestionCompleted={handleQuestionCompleted}
          />
        )}

        {activeTab === 'analysis' && (
          <WeakTopicAnalysis
            onSelectTopic={handleSelectTopic}
            onSelectQuestion={handleSelectQuestion}
          />
        )}

        {activeTab === 'history' && (
          <HistoryView />
        )}
      </main>
    </div>
  );
}
