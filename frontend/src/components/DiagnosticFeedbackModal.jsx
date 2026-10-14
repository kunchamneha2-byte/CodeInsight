import React, { useState } from 'react';
import { 
  CheckCircle2, 
  AlertTriangle, 
  X, 
  Sparkles, 
  Lightbulb, 
  Code, 
  ArrowRight,
  Eye,
  EyeOff
} from 'lucide-react';

export default function DiagnosticFeedbackModal({ result, onClose, onNextQuestion }) {
  const [showSolution, setShowSolution] = useState(false);

  if (!result) return null;

  const isSuccess = result.isCorrect;

  return (
    <div className="feedback-modal-overlay" onClick={onClose}>
      <div className="feedback-card" onClick={(e) => e.stopPropagation()}>
        <div className={`feedback-header ${isSuccess ? 'success' : 'error'}`}>
          <div style={{
            width: 44,
            height: 44,
            borderRadius: 12,
            background: isSuccess ? 'rgba(16, 185, 129, 0.2)' : 'rgba(244, 63, 94, 0.2)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            color: isSuccess ? 'var(--accent-green)' : 'var(--accent-rose)'
          }}>
            {isSuccess ? <CheckCircle2 size={26} /> : <AlertTriangle size={26} />}
          </div>

          <div style={{ flex: 1 }}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem', marginBottom: '0.2rem' }}>
              <span style={{
                fontSize: '0.75rem',
                fontWeight: 700,
                textTransform: 'uppercase',
                padding: '0.2rem 0.5rem',
                borderRadius: 4,
                background: isSuccess ? 'var(--accent-green-bg)' : 'var(--accent-rose-bg)',
                color: isSuccess ? 'var(--accent-green)' : 'var(--accent-rose)',
                border: `1px solid ${isSuccess ? 'var(--accent-green-border)' : 'var(--accent-rose-border)'}`
              }}>
                {isSuccess ? 'Passed' : result.mistakeDisplayName || 'Needs Revision'}
              </span>
              <span style={{
                fontSize: '0.72rem',
                fontWeight: 800,
                textTransform: 'uppercase',
                padding: '0.15rem 0.45rem',
                borderRadius: 4,
                background: 'rgba(255, 255, 255, 0.1)',
                color: '#fff',
                border: '1px solid var(--bg-card-border)'
              }}>
                {result.language || 'JAVA'}
              </span>
              <span style={{ fontSize: '0.85rem', color: 'var(--text-muted)' }}>
                Score: <strong style={{ color: isSuccess ? 'var(--accent-green)' : 'var(--accent-rose)' }}>{result.score}/100</strong>
              </span>
            </div>
            <h3 style={{ fontSize: '1.2rem', fontWeight: 800, color: 'var(--text-primary)' }}>
              {result.feedbackTitle || (isSuccess ? 'Solution Verified!' : 'Diagnostic Review')}
            </h3>
          </div>

          <button 
            onClick={onClose}
            style={{
              background: 'transparent',
              border: 'none',
              color: 'var(--text-muted)',
              cursor: 'pointer',
              padding: 4
            }}
          >
            <X size={20} />
          </button>
        </div>

        <div className="feedback-body">
          {/* Detailed Diagnostic Explanation */}
          <div className={`diagnostic-box ${isSuccess ? 'success' : ''}`}>
            <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.35rem', fontWeight: 700, fontSize: '0.9rem', color: isSuccess ? 'var(--accent-green)' : 'var(--accent-rose)' }}>
              <Sparkles size={16} />
              <span>AI Diagnostic Breakdown</span>
            </div>
            <p style={{ fontSize: '0.88rem', color: 'var(--text-primary)', lineHeight: 1.55 }}>
              {result.feedbackExplanation}
            </p>
          </div>

          {/* Actionable Suggestions */}
          {result.suggestions && (
            <div style={{ background: 'rgba(245, 158, 11, 0.08)', border: '1px solid var(--accent-amber-border)', borderRadius: 'var(--border-radius-md)', padding: '0.85rem 1rem' }}>
              <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '0.25rem', fontWeight: 700, fontSize: '0.85rem', color: 'var(--accent-amber)' }}>
                <Lightbulb size={16} />
                <span>Actionable Recommendation</span>
              </div>
              <p style={{ fontSize: '0.85rem', color: '#fde68a', lineHeight: 1.5 }}>
                {result.suggestions}
              </p>
            </div>
          )}

          {/* Optional Reference Solution Toggle */}
          {result.sampleSolution && !isSuccess && (
            <div>
              <button 
                className="btn-secondary"
                style={{ width: '100%', justifyContent: 'center', fontSize: '0.82rem' }}
                onClick={() => setShowSolution(!showSolution)}
              >
                {showSolution ? <EyeOff size={15} /> : <Eye size={15} />}
                <span>{showSolution ? 'Hide Reference Solution' : 'View Correct Canonical Solution'}</span>
              </button>

              {showSolution && (
                <div style={{ marginTop: '0.75rem' }}>
                  <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', marginBottom: '0.35rem' }}>
                    CANONICAL SOLUTION:
                  </div>
                  <pre className="code-box">
                    <code>{result.sampleSolution}</code>
                  </pre>
                </div>
              )}
            </div>
          )}

          {/* Action Buttons */}
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '0.75rem', marginTop: '0.5rem' }}>
            <button className="btn-secondary" onClick={onClose}>
              {isSuccess ? 'Close' : 'Modify Code & Try Again'}
            </button>
            {isSuccess && onNextQuestion && (
              <button className="btn-primary" onClick={onNextQuestion}>
                <span>Next Challenge</span>
                <ArrowRight size={16} />
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
