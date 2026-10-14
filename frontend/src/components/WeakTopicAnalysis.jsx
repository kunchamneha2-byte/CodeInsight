import React, { useEffect, useState } from 'react';
import { 
  AlertTriangle, 
  CheckCircle2, 
  TrendingDown, 
  TrendingUp, 
  Sparkles, 
  ArrowRight,
  ShieldAlert,
  Brain,
  Layers,
  Repeat,
  Code2,
  GitFork
} from 'lucide-react';
import { api } from '../services/api';

export default function WeakTopicAnalysis({ onSelectTopic, onSelectQuestion }) {
  const [analysis, setAnalysis] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      const [analysisData, recData] = await Promise.all([
        api.getWeakTopicAnalysis(),
        api.getRecommendations()
      ]);
      setAnalysis(analysisData);
      setRecommendation(recData);
    } catch (err) {
      console.error('Failed to load weak topic analysis:', err);
    } finally {
      setLoading(false);
    }
  };

  const getTopicIcon = (slug) => {
    switch (slug?.toLowerCase()) {
      case 'loops': return <Repeat size={20} />;
      case 'arrays': return <Layers size={20} />;
      case 'functions': return <Code2 size={20} />;
      case 'recursion': return <GitFork size={20} />;
      default: return <Code2 size={20} />;
    }
  };

  if (loading) {
    return (
      <div style={{ textAlign: 'center', padding: '4rem 1rem', color: 'var(--text-secondary)' }}>
        Generating weak-topic analysis from persistent database records...
      </div>
    );
  }

  if (!analysis) return null;

  const {
    weakestTopic,
    strongestTopic,
    allTopicProgress = [],
    primaryMistake,
    primaryMistakeName,
    primaryMistakeExplanation,
    diagnosticSummary,
    mistakeBreakdown = []
  } = analysis;

  return (
    <div>
      <div className="page-header">
        <div>
          <h1 className="page-title">Diagnostic & Weak-Topic Analysis</h1>
          <p className="page-subtitle">Algorithmic analysis of your previous submissions to identify conceptual gaps and accelerate mastery.</p>
        </div>
      </div>

      {/* Narrative AI Diagnostic Summary */}
      <div style={{
        background: 'linear-gradient(135deg, rgba(30, 41, 59, 0.7) 0%, rgba(15, 23, 42, 0.8) 100%)',
        border: '1px solid rgba(139, 92, 246, 0.3)',
        borderRadius: 'var(--border-radius-lg)',
        padding: '1.25rem 1.5rem',
        marginBottom: '2rem',
        display: 'flex',
        alignItems: 'center',
        gap: '1rem'
      }}>
        <div style={{
          background: 'linear-gradient(135deg, #8b5cf6 0%, #3b82f6 100%)',
          width: 44,
          height: 44,
          borderRadius: 12,
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          color: '#fff',
          flexShrink: 0
        }}>
          <Brain size={24} />
        </div>
        <div>
          <div style={{ fontSize: '0.78rem', fontWeight: 700, color: '#c4b5fd', textTransform: 'uppercase', letterSpacing: '0.05em' }}>
            Diagnostic Evaluation Summary
          </div>
          <div style={{ fontSize: '0.95rem', color: 'var(--text-primary)', marginTop: '0.2rem', lineHeight: 1.5 }}>
            {diagnosticSummary}
          </div>
        </div>
      </div>

      {/* Top 2 Cards: Weakest Area vs Strongest Area */}
      <div className="analytics-grid">
        {/* Weakest Topic Card */}
        <div className="analysis-card" style={{ borderColor: weakestTopic ? 'var(--accent-rose-border)' : 'var(--bg-card-border)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem', marginBottom: '1rem' }}>
            <div style={{ padding: '0.5rem', borderRadius: 8, background: 'var(--accent-rose-bg)', color: 'var(--accent-rose)' }}>
              <TrendingDown size={22} />
            </div>
            <div>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase' }}>
                Topic Requiring Immediate Focus
              </div>
              <h3 style={{ fontSize: '1.2rem', fontWeight: 800, color: 'var(--text-primary)' }}>
                {weakestTopic ? weakestTopic.topicName : 'No Weak Topic Identified'}
              </h3>
            </div>
          </div>

          {weakestTopic ? (
            <div>
              <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)', marginBottom: '1rem' }}>
                You have recorded <strong>{weakestTopic.failedAttempts} failed attempt(s)</strong> across {weakestTopic.totalAttempts} total submissions in this domain.
              </p>

              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '0.35rem', fontSize: '0.8rem', fontWeight: 600 }}>
                <span>Topic Accuracy</span>
                <span className="text-danger">{weakestTopic.accuracyRate}%</span>
              </div>
              <div className="progress-track" style={{ marginBottom: '1.25rem' }}>
                <div className="progress-fill" style={{ width: `${weakestTopic.accuracyRate}%`, background: 'var(--accent-rose)' }}></div>
              </div>

              <button className="btn-secondary" style={{ width: '100%', justifyContent: 'center' }} onClick={() => onSelectTopic(weakestTopic.topicSlug)}>
                <span>Practice {weakestTopic.topicName} Challenges</span>
                <ArrowRight size={15} />
              </button>
            </div>
          ) : (
            <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)' }}>
              As you practice more questions and submit attempts, CodeInsight will automatically isolate topics where errors cluster.
            </p>
          )}
        </div>

        {/* Primary Mistake Category Card */}
        <div className="analysis-card" style={{ borderColor: primaryMistake ? 'var(--accent-amber-border)' : 'var(--bg-card-border)' }}>
          <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem', marginBottom: '1rem' }}>
            <div style={{ padding: '0.5rem', borderRadius: 8, background: 'var(--accent-amber-bg)', color: 'var(--accent-amber)' }}>
              <ShieldAlert size={22} />
            </div>
            <div>
              <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase' }}>
                Most Frequent Mistake Type
              </div>
              <h3 style={{ fontSize: '1.2rem', fontWeight: 800, color: 'var(--text-primary)' }}>
                {primaryMistakeName || 'No Repeated Mistakes'}
              </h3>
            </div>
          </div>

          {primaryMistakeExplanation ? (
            <div>
              <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)', lineHeight: 1.5, marginBottom: '1rem' }}>
                {primaryMistakeExplanation}
              </p>

              <div style={{ background: 'rgba(245, 158, 11, 0.08)', borderRadius: 8, padding: '0.75rem 1rem', borderLeft: '3px solid var(--accent-amber)' }}>
                <div style={{ fontSize: '0.78rem', fontWeight: 700, color: 'var(--accent-amber)', marginBottom: '0.2rem' }}>
                  Beginner Prevention Strategy
                </div>
                <div style={{ fontSize: '0.82rem', color: '#fde68a' }}>
                  Review edge cases with small values (e.g. n=0, n=1, empty list) on paper before writing code.
                </div>
              </div>
            </div>
          ) : (
            <p style={{ fontSize: '0.88rem', color: 'var(--text-secondary)' }}>
              Zero critical mistake categories recorded yet. Keep up the clean code practices!
            </p>
          )}
        </div>
      </div>

      {/* Topic Breakdown Table & Mistake Distribution */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(400px, 1fr))', gap: '1.5rem', marginBottom: '2.5rem' }}>
        {/* Topic Mastery Spectrum */}
        <div className="analysis-card">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.25rem', display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <span>Topic Mastery Spectrum</span>
          </h3>

          <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
            {allTopicProgress.map((tp) => (
              <div key={tp.topicId} style={{ background: 'rgba(255, 255, 255, 0.02)', padding: '0.85rem 1rem', borderRadius: 'var(--border-radius-md)', border: '1px solid var(--bg-card-border)' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '0.4rem' }}>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', fontWeight: 600 }}>
                    <span style={{ color: tp.color }}>{getTopicIcon(tp.topicSlug)}</span>
                    <span>{tp.topicName}</span>
                  </div>
                  <span style={{
                    fontSize: '0.72rem',
                    fontWeight: 700,
                    padding: '0.2rem 0.5rem',
                    borderRadius: 4,
                    background: tp.masteryStatus === 'Mastered' ? 'var(--accent-green-bg)' :
                                tp.masteryStatus === 'Needs Focus' ? 'var(--accent-rose-bg)' :
                                'rgba(255, 255, 255, 0.05)',
                    color: tp.masteryStatus === 'Mastered' ? 'var(--accent-green)' :
                           tp.masteryStatus === 'Needs Focus' ? 'var(--accent-rose)' :
                           'var(--text-secondary)'
                  }}>
                    {tp.masteryStatus}
                  </span>
                </div>

                <div style={{ display: 'flex', justifyContent: 'space-between', fontSize: '0.78rem', color: 'var(--text-muted)', marginBottom: '0.35rem' }}>
                  <span>{tp.solvedQuestions}/{tp.totalQuestions} Questions Solved</span>
                  <span>Accuracy: <strong>{tp.accuracyRate}%</strong> ({tp.totalAttempts} attempts)</span>
                </div>

                <div className="progress-track">
                  <div className="progress-fill" style={{ width: `${tp.accuracyRate}%`, backgroundColor: tp.color }}></div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* Mistake Category Distribution */}
        <div className="analysis-card">
          <h3 style={{ fontSize: '1.1rem', fontWeight: 700, marginBottom: '1.25rem' }}>
            <span>Error Category Distribution</span>
          </h3>

          {mistakeBreakdown.length > 0 ? (
            <div className="mistake-list">
              {mistakeBreakdown.map((m) => (
                <div key={m.category} className="mistake-row">
                  <div className="mistake-row-header">
                    <span style={{ color: 'var(--text-primary)' }}>{m.categoryName}</span>
                    <span style={{ color: 'var(--accent-rose)', fontWeight: 700 }}>{m.count} error(s) ({m.percentage}%)</span>
                  </div>
                  <div className="progress-track">
                    <div className="progress-fill" style={{ width: `${m.percentage}%`, background: 'var(--accent-rose)' }}></div>
                  </div>
                  <div style={{ fontSize: '0.78rem', color: 'var(--text-muted)' }}>
                    {m.explanation}
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div style={{ padding: '2rem 1rem', textAlign: 'center', color: 'var(--text-muted)', fontSize: '0.88rem' }}>
              No errors recorded yet. Practice questions to view detailed mistake classification.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
