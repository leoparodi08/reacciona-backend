package ar.edu.udemm.reacciona.progress.dto;

import java.util.List;

public record ProgressSummaryDto(int totalModules,
                                  int completedModules,
                                  int inProgressModules,
                                  int notStartedModules,
                                  int totalScore,
                                  List<ModuleProgressDto> modules,
                                  List<AchievementDto> achievements) {}
