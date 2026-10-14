import React from 'react';
import { 
  CheckCircle2, 
  Target, 
  Flame, 
  TrendingUp, 
  ArrowRight, 
  Sparkles, 
  Repeat, 
  Layers, 
  Code2, 
  GitFork, 
  HelpCircle,
  Clock,
  AlertCircle
} from 'lucide-react';

export default function Dashboard({ 
  dashboardData, 
  onSelectTopic, 
  onSelectQuestion, 
  onGoToPractice, 
  onGoToAnalysis 
}) {
  if (!dashboardData) {
    return (
      <div style={{ textAlign: 'center', padding: '4rem 1rem' }}>
        <div style={{ fontSize: '1.25rem', color: 'var(--text-secondary)' }}>Loading student dashboard...</div>
      </div>
    );
  }

  const {
    totalQuestions = 12,
    solvedQuestions = 0,
    totalAttempts = 0,
    overallAccuracy = 0,
    currentStreakDays = 0,
    weakestTopicName,
    strongestTopicName,
    topics = [],
    recentSubmissions = [],
    topRecommendation
  } = dashboardData;

  const getTopicIcon = (slug) => {
    switch (slug?.toLowerCase()) {
      case 'loops': return <Repeat size={22} />;
      case 'arrays': return <Layers size={22} />;
      case 'functions': return <Code2 size={22} />;
      case 'recursion': return <GitFork size={22} />;
      default: return <Code2 size={22} />;
    }
  };

  return (
    <div>
      {/* Page Header */}
      <div className="page-header">
        <div>
          <h1 className="page-title">Student Learning Dashboard</h1>
          <p className="page-subtitle">Track your problem-solving trajectory, master algorithms, and diagnose coding patterns.</p>
        </div>
        <button className="btn-primary" onClick={onGoToPractice}>
          <span>Open Practice Lab</span>
          <ArrowRight size={16} />
        </button>
      </div>

      {/* Top Stats Overview */}
      <div className="stats-grid">
        <div className="stat-card">
          <div className="stat-info">
            <span className="stat-label">Problems Solved</span>
            <div className="stat-value">{solvedQuestions} <span style={{ fontSize: '1.1rem', color: 'var(--text-muted)' }}>/ {totalQuestions}</span></div>
            <span className="stat-subtext">{Math.round((solvedQuestions / (totalQuestions || 1)) * 100)}% overall completion</span>
          </div>
          <div className="stat-icon-wrapper" style={{ background: 'var(--accent-green-bg)', color: 'var(--accent-green)' }}>
            <CheckCircle2 size={26} />
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-info">
            <span className="stat-label">Accuracy Rate</span>
            <div className="stat-value">{overallAccuracy}%</div>
            <span className="stat-subtext">{totalAttempts} total submissions</span>
          </div>
          <div className="stat-icon-wrapper" style={{ background: 'var(--accent-blue-glow)', color: 'var(--accent-blue)' }}>
            <Target size={26} />
          </div>
        </div>

        <div className="stat-card">
          <div className="stat-info">
            <span className="stat-label">Practice Streak</span>
            <div className="stat-value">{currentStreakDays} <span style={{ fontSize: '1.1rem', color: 'var(--text-muted)' }}>days</span></div>
            <span className="stat-subtext">Active persistence streak</span>
          </div>
          <div className="stat-icon-wrapper" style={{ background: 'var(--accent-amber-bg)', color: 'var(--accent-amber)' }}>
            <Flame size={26} />
          </div>
        </div>

        <div className="stat-card" style={{ cursor: 'pointer' }} onClick={onGoToAnalysis}>
          <div className="stat-info">
            <span className="stat-label">Focus Area</span>
            <div className="stat-value" style={{ fontSize: '1.25rem', marginTop: '0.2rem' }}>
              {weakestTopicName ? weakestTopicName : 'Balanced Progress'}
            </div>
            <span className="stat-subtext text-blue">View diagnostic insights →</span>
          </div>
          <div className="stat-icon-wrapper" style={{ background: 'var(--accent-purple-bg)', color: 'var(--accent-purple)' }}>
            <TrendingUp size={26} />
          </div>
        </div>
      </div>

      {/* AI Recommendation Banner */}
      {topRecommendation && (
        <div className="recommendation-banner">
          <div className="rec-avatar">
            <Sparkles size={24} />
          </div>
          <div className="rec-content">
            <div className="rec-badge-row">
              <span className="rec-tag">AI Personalized Recommendation</span>
              <span style={{
                fontSize: '0.72rem',
                fontWeight: 700,
                color: topRecommendation.priorityLevel === 'HIGH' ? 'var(--accent-rose)' : 'var(--accent-amber)',
                textTransform: 'uppercase'
              }}>
                {topRecommendation.priorityLevel} Priority
              </span>
            </div>
            <h2 className="rec-title">{topRecommendation.targetTopicName}</h2>
            <p className="rec-rationale">{topRecommendation.rationale}</p>

            {topRecommendation.learningTip && (
              <div className="rec-tip-box">
                <strong>Targeted Learning Tip:</strong> {topRecommendation.learningTip}
              </div>
            )}

            <div className="rec-actions">
              {topRecommendation.recommendedQuestions?.map((q) => (
                <button
                  key={q.id}
                  className="btn-primary"
                  style={{ fontSize: '0.82rem', padding: '0.5rem 1rem' }}
                  onClick={() => onSelectQuestion(q.id)}
                >
                  <span>Solve: {q.title}</span>
                  <ArrowRight size={14} />
                </button>
              ))}
              <button className="btn-secondary" onClick={() => onSelectTopic(topRecommendation.targetTopicSlug)}>
                View All {topRecommendation.targetTopicName}
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Core Topics Grid */}
      <div style={{ marginBottom: '2.5rem' }}>
        <h2 className="section-title">
          <span>Core Coding Curriculum</span>
        </h2>
        <div className="topic-grid">
          {topics.map((topic) => {
            const mastery = topic.masteryPercentage || 0;
            return (
              <div 
                key={topic.id} 
                className="topic-card" 
                style={{ '--card-accent-color': topic.color }}
                onClick={() => onSelectTopic(topic.slug)}
              >
                <div className="topic-card-header">
                  <div className="topic-icon" style={{ background: `${topic.color}20`, color: topic.color }}>
                    {getTopicIcon(topic.slug)}
                  </div>
                  <span className="topic-badge">
                    {topic.solvedQuestions} / {topic.totalQuestions} Solved
                  </span>
                </div>

                <div className="topic-name">{topic.name}</div>
                <div className="topic-desc">{topic.description}</div>

                <div className="progress-bar-container">
                  <div className="progress-header">
                    <span>Mastery</span>
                    <span style={{ color: topic.color }}>{mastery}%</span>
                  </div>
                  <div className="progress-track">
                    <div 
                      className="progress-fill" 
                      style={{ width: `${mastery}%`, backgroundColor: topic.color }}
                    ></div>
                  </div>
                </div>

                <button className="btn-card-action">
                  <span>Enter Practice Lab</span>
                  <ArrowRight size={15} />
                </button>
              </div>
            );
          })}
        </div>
      </div>

      {/* Recent Submissions History */}
      {recentSubmissions && recentSubmissions.length > 0 && (
        <div>
          <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1rem' }}>
            <h2 className="section-title" style={{ margin: 0 }}>Recent Attempt Log</h2>
            <button className="btn-secondary" style={{ fontSize: '0.8rem' }} onClick={onGoToAnalysis}>
              <span>View Weak-Topic Breakdown →</span>
            </button>
          </div>

          <div className="table-container">
            <table className="custom-table">
              <thead>
                <tr>
                  <th>Status</th>
                  <th>Language</th>
                  <th>Question</th>
                  <th>Topic</th>
                  <th>Diagnostic Diagnosis</th>
                  <th>Score</th>
                  <th>Submitted At</th>
                </tr>
              </thead>
              <tbody>
                {recentSubmissions.map((sub) => (
                  <tr key={sub.id}>
                    <td>
                      <span style={{
                        display: 'inline-flex',
                        alignItems: 'center',
                        gap: '0.35rem',
                        fontSize: '0.75rem',
                        fontWeight: 700,
                        padding: '0.2rem 0.55rem',
                        borderRadius: 4,
                        background: sub.isCorrect ? 'var(--accent-green-bg)' : 'var(--accent-rose-bg)',
                        color: sub.isCorrect ? 'var(--accent-green)' : 'var(--accent-rose)',
                        border: `1px solid ${sub.isCorrect ? 'var(--accent-green-border)' : 'var(--accent-rose-border)'}`
                      }}>
                        {sub.isCorrect ? <CheckCircle2 size={13} /> : <AlertCircle size={13} />}
                        {sub.isCorrect ? 'Accepted' : 'Needs Fix'}
                      </span>
                    </td>
                    <td>
                      <span style={{
                        fontSize: '0.72rem',
                        fontWeight: 800,
                        padding: '0.15rem 0.5rem',
                        borderRadius: 4,
                        background: 'rgba(255, 255, 255, 0.08)',
                        color: sub.language === 'PYTHON' ? '#34d399' :
                               sub.language === 'C' ? '#38bdf8' :
                               sub.language === 'CPP' ? '#818cf8' : '#fb923c',
                        border: '1px solid rgba(255, 255, 255, 0.1)'
                      }}>
                        {sub.language || 'JAVA'}
                      </span>
                    </td>
                    <td style={{ fontWeight: 600, color: 'var(--text-primary)' }}>
                      <span 
                        style={{ cursor: 'pointer', textDecoration: 'underline' }}
                        onClick={() => onSelectQuestion(sub.questionId)}
                      >
                        {sub.questionTitle}
                      </span>
                    </td>
                    <td>{sub.topicName}</td>
                    <td>
                      <span style={{ color: sub.isCorrect ? 'var(--accent-green)' : '#fca5a5' }}>
                        {sub.isCorrect ? 'Verified Optimal' : sub.mistakeDisplayName}
                      </span>
                    </td>
                    <td style={{ fontWeight: 700 }}>{sub.score}/100</td>
                    <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                      {new Date(sub.submittedAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
}
