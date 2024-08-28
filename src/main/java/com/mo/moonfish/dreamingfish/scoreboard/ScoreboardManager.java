package com.mo.moonfish.dreamingfish.scoreboard;

import com.mo.moonfish.dreamingfish.economy.EconomyManager;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;

public class ScoreboardManager {

    public static void initializeScoreboard(ServerPlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();

        // 清除旧的计分板条目，避免重复添加
        ScoreboardObjective objective = scoreboard.getObjective("PowerBoard");
        if (objective != null) {
            scoreboard.removeObjective(objective);
        }

        // 创建新的计分板
        objective = scoreboard.addObjective("PowerBoard", ScoreboardCriterion.DUMMY, Text.literal("PowerBoard").formatted(Formatting.GOLD), ScoreboardCriterion.RenderType.INTEGER);

        scoreboard.setObjectiveSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID, objective); // 设置显示位置，例如侧边栏

        updateScoreboard(player);
    }

    public static void updateScoreboard(ServerPlayerEntity player) {
        Scoreboard scoreboard = player.getScoreboard();
        ScoreboardObjective objective = scoreboard.getObjective("PowerBoard");

        if (objective != null) {
            // 重置分数条目，避免重复添加
            resetScores(scoreboard, objective);

            // 例如，显示玩家名称
            setScore(scoreboard, objective, player.getEntityName(), 10);

            // 显示经济信息，例如余额
            double balance = getBalance(player); // 需要从你的经济系统中获取
            setScore(scoreboard, objective, "余额: " + String.format("%.2f", balance), 9);

            // 添加更多的行
        }
    }

    private static void resetScores(Scoreboard scoreboard, ScoreboardObjective objective) {
        Collection<ScoreboardPlayerScore> scores = scoreboard.getAllPlayerScores(objective);
        for (ScoreboardPlayerScore score : scores) {
            scoreboard.resetPlayerScore(score.getPlayerName(), objective);
        }
    }

    private static void setScore(Scoreboard scoreboard, ScoreboardObjective objective, String name, int score) {
        ScoreboardPlayerScore playerScore = scoreboard.getPlayerScore(name, objective);
        playerScore.setScore(score);
    }

    private static double getBalance(ServerPlayerEntity player) {
        // 在此处获取玩家的余额
        return EconomyManager.get(player.getServer()).getBalance(player); // 示例值
    }
}
