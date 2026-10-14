import React, { useState, useEffect } from 'react';
import { 
  Play, 
  RotateCcw, 
  CheckCircle2, 
  HelpCircle, 
  Sparkles, 
  Terminal, 
  ChevronDown, 
  ChevronUp,
  AlertTriangle,
  Lightbulb,
  FileCode,
  Languages
} from 'lucide-react';
import { api } from '../services/api';
import DiagnosticFeedbackModal from './DiagnosticFeedbackModal';

const SUPPORTED_LANGUAGES = [
  { id: 'JAVA', name: 'Java', version: 'JDK 17/21', file: 'Solution.java', color: '#f97316' },
  { id: 'PYTHON', name: 'Python', version: 'Python 3.x', file: 'solution.py', color: '#10b981' },
  { id: 'C', name: 'C', version: 'C17 standard', file: 'solution.c', color: '#06b6d4' },
  { id: 'CPP', name: 'C++', version: 'C++20', file: 'solution.cpp', color: '#3b82f6' },
];

export default function PracticeView({ 
  selectedQuestionId, 
  initialTopicSlug, 
  onQuestionCompleted 
}) {
  const [questions, setQuestions] = useState([]);
  const [currentQuestion, setCurrentQuestion] = useState(null);
  const [selectedLanguage, setSelectedLanguage] = useState('JAVA');
  const [code, setCode] = useState('');
  const [filterTopic, setFilterTopic] = useState(initialTopicSlug || 'ALL');
  const [filterDiff, setFilterDiff] = useState('ALL');
  const [showHint, setShowHint] = useState(false);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [diagnosticResult, setDiagnosticResult] = useState(null);
  const [loading, setLoading] = useState(true);

  // Cache user code per question and language to preserve in-progress work
  const [codeCache, setCodeCache] = useState({});

  // Load questions on mount or filter change
  useEffect(() => {
    loadQuestions();
  }, [filterTopic, filterDiff]);

  // When selectedQuestionId prop changes or questions list updates
  useEffect(() => {
    if (selectedQuestionId) {
      loadQuestionDetail(selectedQuestionId);
    } else if (questions.length > 0 && !currentQuestion) {
      loadQuestionDetail(questions[0].id);
    }
  }, [selectedQuestionId, questions]);

  const loadQuestions = async () => {
    try {
      setLoading(true);
      const data = await api.getQuestions({
        topicSlug: filterTopic === 'ALL' ? null : filterTopic,
        difficulty: filterDiff === 'ALL' ? null : filterDiff,
      });
      setQuestions(data);
    } catch (err) {
      console.error('Failed to load questions:', err);
    } finally {
      setLoading(false);
    }
  };

  const loadQuestionDetail = async (id, lang = selectedLanguage) => {
    try {
      const q = await api.getQuestionById(id);
      setCurrentQuestion(q);
      
      const targetLang = q.lastSubmittedLanguage || lang || 'JAVA';
      setSelectedLanguage(targetLang);

      // Check cache first, then last submitted, then template
      const cacheKey = `${id}-${targetLang}`;
      if (codeCache[cacheKey]) {
        setCode(codeCache[cacheKey]);
      } else if (q.lastSubmittedCode && q.lastSubmittedLanguage === targetLang) {
        setCode(q.lastSubmittedCode);
      } else {
        const template = (q.starterCodes && q.starterCodes[targetLang]) || q.starterCode;
        setCode(template);
      }
      setShowHint(false);
    } catch (err) {
      console.error('Failed to load question details:', err);
    }
  };

  // When user switches programming language dropdown
  const handleLanguageChange = (newLang) => {
    if (!currentQuestion) return;

    // Save current code to cache
    const currentKey = `${currentQuestion.id}-${selectedLanguage}`;
    setCodeCache(prev => ({ ...prev, [currentKey]: code }));

    setSelectedLanguage(newLang);

    // Retrieve cached code for new language, or fall back to template
    const newKey = `${currentQuestion.id}-${newLang}`;
    if (codeCache[newKey]) {
      setCode(codeCache[newKey]);
    } else {
      const template = (currentQuestion.starterCodes && currentQuestion.starterCodes[newLang]) 
        || currentQuestion.starterCode;
      setCode(template);
    }
  };

  const handleResetCode = () => {
    if (!currentQuestion) return;
    const currentLangObj = SUPPORTED_LANGUAGES.find(l => l.id === selectedLanguage) || SUPPORTED_LANGUAGES[0];
    if (window.confirm(`Reset ${currentLangObj.name} code back to the starter template?`)) {
      const template = (currentQuestion.starterCodes && currentQuestion.starterCodes[selectedLanguage]) 
        || currentQuestion.starterCode;
      setCode(template);
      
      const key = `${currentQuestion.id}-${selectedLanguage}`;
      setCodeCache(prev => {
        const copy = { ...prev };
        delete copy[key];
        return copy;
      });
    }
  };

  const handleSubmit = async () => {
    if (!currentQuestion) return;
    try {
      setIsSubmitting(true);
      const res = await api.submitSolution(currentQuestion.id, code, 'student-1', selectedLanguage);
      setDiagnosticResult(res);
      
      // Update cache
      const key = `${currentQuestion.id}-${selectedLanguage}`;
      setCodeCache(prev => ({ ...prev, [key]: code }));

      // Reload current question to update state
      await loadQuestionDetail(currentQuestion.id, selectedLanguage);
      await loadQuestions();
      if (onQuestionCompleted) onQuestionCompleted();
    } catch (err) {
      alert('Error submitting code: ' + err.message);
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleNextQuestion = () => {
    setDiagnosticResult(null);
    if (!currentQuestion) return;
    const currentIndex = questions.findIndex(q => q.id === currentQuestion.id);
    if (currentIndex >= 0 && currentIndex < questions.length - 1) {
      loadQuestionDetail(questions[currentIndex + 1].id, selectedLanguage);
    }
  };

  const getDifficultyClass = (diff) => {
    switch (diff) {
      case 'EASY': return 'diff-easy';
      case 'MEDIUM': return 'diff-medium';
      case 'HARD': return 'diff-hard';
      default: return 'diff-easy';
    }
  };

  // Language info helpers
  const currentLangObj = SUPPORTED_LANGUAGES.find(l => l.id === selectedLanguage) || SUPPORTED_LANGUAGES[0];
  const currentFileName = (currentQuestion?.fileNames && currentQuestion.fileNames[selectedLanguage]) 
    || currentLangObj.file;
  
  // Language-specific instructions
  const currentPrompt = (currentQuestion?.prompts && currentQuestion.prompts[selectedLanguage])
    || currentQuestion?.prompt;

  return (
    <div>
      <div className="practice-layout">
        {/* Left Sidebar: Filters and Question List */}
        <aside className="sidebar-questions">
          <div className="filter-group">
            <label className="filter-label">Filter by Topic</label>
            <select 
              className="filter-select"
              value={filterTopic} 
              onChange={(e) => setFilterTopic(e.target.value)}
            >
              <option value="ALL">All Core Topics</option>
              <option value="loops">Loops & Iteration</option>
              <option value="arrays">Arrays & Data Structures</option>
              <option value="functions">Functions & Methods</option>
              <option value="recursion">Recursion & Divide-and-Conquer</option>
            </select>
          </div>

          <div className="filter-group">
            <label className="filter-label">Filter by Difficulty</label>
            <select 
              className="filter-select"
              value={filterDiff} 
              onChange={(e) => setFilterDiff(e.target.value)}
            >
              <option value="ALL">All Difficulties</option>
              <option value="EASY">Easy</option>
              <option value="MEDIUM">Medium</option>
              <option value="HARD">Hard</option>
            </select>
          </div>

          <div className="question-list">
            <div style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', marginBottom: '0.25rem' }}>
              PRACTICE QUESTIONS ({questions.length})
            </div>

            {loading ? (
              <div style={{ padding: '1rem', color: 'var(--text-muted)', fontSize: '0.85rem' }}>Loading questions...</div>
            ) : (
              questions.map((q) => {
                const isActive = currentQuestion && currentQuestion.id === q.id;
                return (
                  <div
                    key={q.id}
                    className={`question-item ${isActive ? 'active' : ''}`}
                    onClick={() => loadQuestionDetail(q.id, selectedLanguage)}
                  >
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', overflow: 'hidden' }}>
                      {q.solved ? (
                        <CheckCircle2 size={16} className="text-success" style={{ flexShrink: 0 }} />
                      ) : (
                        <div style={{ width: 16, height: 16, borderRadius: '50%', border: '2px solid var(--bg-card-border)', flexShrink: 0 }}></div>
                      )}
                      <span className="q-title" style={{ whiteSpace: 'nowrap', textOverflow: 'ellipsis', overflow: 'hidden' }}>
                        {q.title}
                      </span>
                    </div>

                    <span className={`difficulty-pill ${getDifficultyClass(q.difficulty)}`}>
                      {q.difficulty}
                    </span>
                  </div>
                );
              })
            )}
          </div>
        </aside>

        {/* Right Pane: Problem Solver Workspace */}
        <section className="solver-workspace">
          {currentQuestion ? (
            <>
              {/* Problem Workspace Header */}
              <div className="solver-header">
                <div>
                  <div style={{ display: 'flex', alignItems: 'center', gap: '0.65rem', marginBottom: '0.35rem' }}>
                    <span className={`difficulty-pill ${getDifficultyClass(currentQuestion.difficulty)}`}>
                      {currentQuestion.difficulty}
                    </span>
                    <span style={{ fontSize: '0.8rem', fontWeight: 600, color: 'var(--text-muted)' }}>
                      Topic: {currentQuestion.topicName}
                    </span>
                    {currentQuestion.solved && (
                      <span style={{ display: 'inline-flex', alignItems: 'center', gap: '0.3rem', fontSize: '0.75rem', color: 'var(--accent-green)', fontWeight: 700 }}>
                        <CheckCircle2 size={14} /> Solved
                      </span>
                    )}
                  </div>
                  <h2 style={{ fontSize: '1.45rem', fontWeight: 800, color: 'var(--text-primary)' }}>
                    {currentQuestion.title}
                  </h2>
                </div>

                <div style={{ display: 'flex', gap: '0.5rem' }}>
                  <button 
                    className="btn-secondary" 
                    onClick={handleResetCode}
                    title="Reset to starter code"
                  >
                    <RotateCcw size={15} />
                    <span>Reset</span>
                  </button>
                  <button 
                    className="btn-primary" 
                    onClick={handleSubmit}
                    disabled={isSubmitting}
                  >
                    <Play size={15} />
                    <span>{isSubmitting ? 'Evaluating...' : `Submit (${currentLangObj.name})`}</span>
                  </button>
                </div>
              </div>

              {/* Problem Solver Body (Split Pane: Specs & Editor) */}
              <div className="solver-body">
                {/* Left: Language-Specific Problem Specs */}
                <div className="problem-pane">
                  {/* Language Mode Indicator Banner */}
                  <div style={{ 
                    display: 'flex', 
                    alignItems: 'center', 
                    justifyContent: 'space-between',
                    background: `${currentLangObj.color}15`, 
                    border: `1px solid ${currentLangObj.color}40`,
                    padding: '0.5rem 0.85rem',
                    borderRadius: 'var(--border-radius-sm)',
                    marginBottom: '1rem',
                    fontSize: '0.82rem'
                  }}>
                    <span style={{ color: currentLangObj.color, fontWeight: 700, display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                      <Languages size={15} />
                      <span>{currentLangObj.name} Mode Active</span>
                    </span>
                    <span style={{ color: 'var(--text-muted)', fontSize: '0.75rem' }}>
                      {currentLangObj.version}
                    </span>
                  </div>

                  <div style={{ whiteSpace: 'pre-line', fontSize: '0.92rem', color: 'var(--text-primary)', lineHeight: 1.6, marginBottom: '1.5rem' }}>
                    {currentPrompt}
                  </div>

                  {currentQuestion.expectedBehavior && (
                    <div style={{ marginBottom: '1.5rem', background: 'rgba(59, 130, 246, 0.08)', border: '1px solid rgba(59, 130, 246, 0.25)', borderRadius: 'var(--border-radius-md)', padding: '0.85rem 1rem' }}>
                      <div style={{ fontSize: '0.8rem', fontWeight: 700, color: '#93c5fd', textTransform: 'uppercase', marginBottom: '0.25rem' }}>
                        Expected Behavior
                      </div>
                      <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                        {currentQuestion.expectedBehavior}
                      </div>
                    </div>
                  )}

                  {/* Expandable Hint Section */}
                  {currentQuestion.hint && (
                    <div style={{ marginBottom: '1rem', border: '1px solid var(--bg-card-border)', borderRadius: 'var(--border-radius-md)', overflow: 'hidden' }}>
                      <button
                        onClick={() => setShowHint(!showHint)}
                        style={{
                          width: '100%',
                          background: 'rgba(255, 255, 255, 0.03)',
                          border: 'none',
                          padding: '0.75rem 1rem',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'space-between',
                          color: 'var(--accent-amber)',
                          fontSize: '0.85rem',
                          fontWeight: 600,
                          cursor: 'pointer'
                        }}
                      >
                        <div style={{ display: 'flex', alignItems: 'center', gap: '0.45rem' }}>
                          <Lightbulb size={16} />
                          <span>Need a Hint?</span>
                        </div>
                        {showHint ? <ChevronUp size={16} /> : <ChevronDown size={16} />}
                      </button>

                      {showHint && (
                        <div style={{ padding: '0.85rem 1rem', background: 'rgba(245, 158, 11, 0.05)', fontSize: '0.85rem', color: '#fde68a', lineHeight: 1.5 }}>
                          {currentQuestion.hint}
                        </div>
                      )}
                    </div>
                  )}

                  {currentQuestion.solutionExplanation && currentQuestion.solved && (
                    <div style={{ background: 'rgba(16, 185, 129, 0.06)', border: '1px solid var(--accent-green-border)', borderRadius: 'var(--border-radius-md)', padding: '0.85rem 1rem' }}>
                      <div style={{ fontSize: '0.8rem', fontWeight: 700, color: 'var(--accent-green)', textTransform: 'uppercase', marginBottom: '0.25rem' }}>
                        Algorithmic Insight
                      </div>
                      <div style={{ fontSize: '0.85rem', color: 'var(--text-secondary)' }}>
                        {currentQuestion.solutionExplanation}
                      </div>
                    </div>
                  )}
                </div>

                {/* Right: Code Editor with Language Selector Toolbar */}
                <div className="editor-pane">
                  {/* LANGUAGE SELECTION DROPDOWN TOOLBAR ABOVE EDITOR */}
                  <div className="editor-toolbar" style={{ borderBottom: `2px solid ${currentLangObj.color}60` }}>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.75rem' }}>
                      <div style={{ display: 'flex', alignItems: 'center', gap: '0.4rem', color: currentLangObj.color }}>
                        <FileCode size={16} />
                        <span style={{ fontWeight: 700, fontFamily: 'var(--font-mono)' }}>{currentFileName}</span>
                      </div>
                    </div>

                    {/* Language Dropdown Selector */}
                    <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
                      <label 
                        htmlFor="language-select" 
                        style={{ fontSize: '0.75rem', fontWeight: 700, color: 'var(--text-muted)', textTransform: 'uppercase', letterSpacing: '0.04em' }}
                      >
                        Language:
                      </label>
                      <select
                        id="language-select"
                        value={selectedLanguage}
                        onChange={(e) => handleLanguageChange(e.target.value)}
                        style={{
                          background: 'var(--bg-input)',
                          color: '#fff',
                          border: `1px solid ${currentLangObj.color}80`,
                          padding: '0.35rem 0.75rem',
                          borderRadius: '6px',
                          fontSize: '0.85rem',
                          fontWeight: 700,
                          cursor: 'pointer',
                          outline: 'none',
                          boxShadow: `0 0 10px ${currentLangObj.color}25`
                        }}
                      >
                        {SUPPORTED_LANGUAGES.map(lang => (
                          <option key={lang.id} value={lang.id}>
                            {lang.name} ({lang.version})
                          </option>
                        ))}
                      </select>
                    </div>
                  </div>

                  <textarea
                    className="code-textarea"
                    value={code}
                    onChange={(e) => setCode(e.target.value)}
                    placeholder={`// Write your ${currentLangObj.name} solution here...`}
                    spellCheck="false"
                    style={{
                      borderLeft: `3px solid ${currentLangObj.color}40`,
                    }}
                  />

                  <div className="editor-footer">
                    <span style={{ fontSize: '0.75rem', color: 'var(--text-muted)', display: 'flex', alignItems: 'center', gap: '0.4rem' }}>
                      <span style={{ width: 8, height: 8, borderRadius: '50%', background: currentLangObj.color }}></span>
                      <span>{currentLangObj.name} rules active • Static diagnostics on submit</span>
                    </span>
                    <button 
                      className="btn-primary"
                      onClick={handleSubmit}
                      disabled={isSubmitting}
                      style={{ padding: '0.5rem 1.15rem', background: currentLangObj.color }}
                    >
                      <Play size={14} />
                      <span>{isSubmitting ? 'Evaluating...' : `Run & Submit (${currentLangObj.name})`}</span>
                    </button>
                  </div>
                </div>
              </div>
            </>
          ) : (
            <div style={{ padding: '3rem', textAlign: 'center', color: 'var(--text-secondary)' }}>
              Select a question from the sidebar to begin practice.
            </div>
          )}
        </section>
      </div>

      {/* Diagnostic Evaluation Modal */}
      {diagnosticResult && (
        <DiagnosticFeedbackModal
          result={diagnosticResult}
          language={selectedLanguage}
          onClose={() => setDiagnosticResult(null)}
          onNextQuestion={handleNextQuestion}
        />
      )}
    </div>
  );
}
