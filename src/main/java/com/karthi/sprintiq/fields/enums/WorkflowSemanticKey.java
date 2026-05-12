package com.karthi.sprintiq.fields.enums;

/**
 * Stable workflow semantic keys used for cross-project reporting and burndown charts.
 * These are product-owned and intentionally small — never add new values casually.
 * UI labels (e.g. "Open", "Backlog", "Ready for QA") are stored as FieldOption/ProjectFieldOption data.
 */
public enum WorkflowSemanticKey {
  TODO,
  IN_PROGRESS,
  DONE,
  BLOCKED,
  CANCELLED,
}
