const API_BASE = import.meta.env.VITE_API_BASE_URL || '/api';

export const api = {
  // Topics
  async getTopics(studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/topics?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch topics: ${res.statusText}`);
    return res.json();
  },

  async getTopicBySlug(slug, studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/topics/${encodeURIComponent(slug)}?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch topic: ${res.statusText}`);
    return res.json();
  },

  // Questions
  async getQuestions({ topicId, topicSlug, difficulty, studentId = 'student-1' } = {}) {
    const params = new URLSearchParams();
    if (topicId) params.append('topicId', topicId);
    if (topicSlug) params.append('topicSlug', topicSlug);
    if (difficulty) params.append('difficulty', difficulty);
    params.append('studentId', studentId);

    const res = await fetch(`${API_BASE}/questions?${params.toString()}`);
    if (!res.ok) throw new Error(`Failed to fetch questions: ${res.statusText}`);
    return res.json();
  },

  async getQuestionById(id, studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/questions/${id}?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch question: ${res.statusText}`);
    return res.json();
  },

  // Submissions (now includes language)
  async submitSolution(questionId, submittedCode, studentId = 'student-1', language = 'JAVA') {
    const res = await fetch(`${API_BASE}/submissions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        questionId,
        submittedCode,
        studentIdentifier: studentId,
        language: language.toUpperCase(),
      }),
    });

    if (!res.ok) {
      const errorData = await res.json().catch(() => ({}));
      throw new Error(errorData.message || `Submission failed: ${res.statusText}`);
    }

    return res.json();
  },

  async getSubmissionHistory(studentId = 'student-1', questionId = null) {
    const params = new URLSearchParams({ studentId });
    if (questionId) params.append('questionId', questionId);

    const res = await fetch(`${API_BASE}/submissions/history?${params.toString()}`);
    if (!res.ok) throw new Error(`Failed to fetch submission history: ${res.statusText}`);
    return res.json();
  },

  // Analytics
  async getDashboardSummary(studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/analytics/dashboard?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch dashboard summary: ${res.statusText}`);
    return res.json();
  },

  async getWeakTopicAnalysis(studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/analytics/weak-topics?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch weak-topic analysis: ${res.statusText}`);
    return res.json();
  },

  async getRecommendations(studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/analytics/recommendations?studentId=${encodeURIComponent(studentId)}`);
    if (!res.ok) throw new Error(`Failed to fetch recommendations: ${res.statusText}`);
    return res.json();
  },

  async resetProgress(studentId = 'student-1') {
    const res = await fetch(`${API_BASE}/analytics/reset?studentId=${encodeURIComponent(studentId)}`, {
      method: 'POST',
    });
    if (!res.ok) throw new Error(`Failed to reset progress: ${res.statusText}`);
    return res.json();
  },
};
