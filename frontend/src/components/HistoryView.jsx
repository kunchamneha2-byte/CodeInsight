import React, { useState, useEffect } from 'react';
import { 
  CheckCircle2, 
  AlertCircle, 
  Code, 
  X, 
  Terminal,
  Calendar,
  Sparkles
} from 'lucide-react';
import { api } from '../services/api';

export default function HistoryView() {
  const [history, setHistory] = useState([]);
  const [selectedSubmission, setSelectedSubmission] = useState(null);
  const [filter, setFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    try {
      setLoading(true);
      const data = await api.getSubmissionHistory();
      setHistory(data);
    } catch (err) {
      console.error('Failed to load history:', err);
    } finally {
      setLoading(false);
    }
  };

  const filteredHistory = history.filter((sub) => {
    if (filter === 'ACCEPTED') return sub.isCorrect;
    if (filter === 'FAILED') return !sub.isCorrect;
    return true;
  });

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Attempt History & Audit Log</h1>
          <p className="page-subtitle">Every code submission is persistently recorded in MySQL with timestamped diagnostic telemetry.</p>
        </div>

        <div style={{ display: 'flex', gap: '0.5rem' }}>
          <button 
            className={`btn-secondary ${filter === 'ALL' ? 'active' : ''}`}
            onClick={() => setFilter('ALL')}
            style={{ background: filter === 'ALL' ? 'rgba(59, 130, 246, 0.2)' : undefined }}
          >
            All ({history.length})
          </button>
          <button 
            className={`btn-secondary ${filter === 'ACCEPTED' ? 'active' : ''}`}
            onClick={() => setFilter('ACCEPTED')}
            style={{ background: filter === 'ACCEPTED' ? 'rgba(16, 185, 129, 0.2)' : undefined }}
          >
            Accepted ({history.filter(h => h.isCorrect).length})
          </button>
          <button 
            className={`btn-secondary ${filter === 'FAILED' ? 'active' : ''}`}
            onClick={() => setFilter('FAILED')}
            style={{ background: filter === 'FAILED' ? 'rgba(244, 63, 94, 0.2)' : undefined }}
          >
            Needs Review ({history.filter(h => !h.isCorrect).length})
          </button>
        </div>
      </div>

      <div className="table-container">
        {loading ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-muted)' }}>
            Loading persistent records from MySQL...
          </div>
        ) : filteredHistory.length === 0 ? (
          <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-muted)' }}>
            No submissions found matching the selected filter.
          </div>
        ) : (
          <table className="custom-table">
            <thead>
              <tr>
                <th>Status</th>
                <th>Language</th>
                <th>Question</th>
                <th>Topic</th>
                <th>Diagnostic Result</th>
                <th>Score</th>
                <th>Timestamp</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredHistory.map((sub) => (
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
                    {sub.questionTitle}
                  </td>
                  <td>{sub.topicName}</td>
                  <td>
                    <span style={{ color: sub.isCorrect ? 'var(--accent-green)' : '#fca5a5', fontWeight: 500 }}>
                      {sub.isCorrect ? 'Verified Optimal' : sub.mistakeDisplayName}
                    </span>
                  </td>
                  <td style={{ fontWeight: 700 }}>{sub.score}/100</td>
                  <td style={{ fontSize: '0.8rem', color: 'var(--text-muted)' }}>
                    {new Date(sub.submittedAt).toLocaleString()}
                  </td>
                  <td>
                    <button 
                      className="btn-secondary" 
                      style={{ fontSize: '0.75rem', padding: '0.3rem 0.65rem' }}
                      onClick={() => setSelectedSubmission(sub)}
                    >
                      <Code size={13} />
                      <span>Inspect</span>
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>

      {/* Code Inspection Modal */}
      {selectedSubmission && (
        <div className="feedback-modal-overlay" onClick={() => setSelectedSubmission(null)}>
          <div className="feedback-card" onClick={(e) => e.stopPropagation()} style={{ maxWidth: 700 }}>
            <div className={`feedback-header ${selectedSubmission.isCorrect ? 'success' : 'error'}`}>
              <div>
                <h3 style={{ fontSize: '1.2rem', fontWeight: 800 }}>
                  Submission #{selectedSubmission.id}: {selectedSubmission.questionTitle}
                </h3>
                <div style={{ fontSize: '0.8rem', color: 'var(--text-muted)', marginTop: '0.2rem' }}>
                  Submitted on {new Date(selectedSubmission.submittedAt).toLocaleString()}
                </div>
              </div>
              <button 
                onClick={() => setSelectedSubmission(null)}
                style={{ background: 'transparent', border: 'none', color: 'var(--text-muted)', cursor: 'pointer' }}
              >
                <X size={20} />
              </button>
            </div>

            <div className="feedback-body">
              <div className={`diagnostic-box ${selectedSubmission.isCorrect ? 'success' : ''}`}>
                <div style={{ fontWeight: 700, marginBottom: '0.25rem', color: selectedSubmission.isCorrect ? 'var(--accent-green)' : 'var(--accent-rose)' }}>
                  {selectedSubmission.feedbackTitle}
                </div>
                <p style={{ fontSize: '0.88rem', color: 'var(--text-primary)' }}>
                  {selectedSubmission.feedbackExplanation}
                </p>
              </div>

              {selectedSubmission.suggestions && (
                <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                  <strong>Recommendation:</strong> {selectedSubmission.suggestions}
                </div>
              )}

              <div style={{ display: 'flex', justifyContent: 'flex-end', marginTop: '0.5rem' }}>
                <button className="btn-secondary" onClick={() => setSelectedSubmission(null)}>
                  Close
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
