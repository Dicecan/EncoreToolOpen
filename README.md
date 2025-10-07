# EncoreToolOpen
An open-source conceptual implementation of the core player locating algorithm for EncoreTool.EncoreTool 核心玩家定位算法的开源思路实现。

## 声明 (Disclaimer)

**此仓库中的代码仅为算法思路的伪代码实现，并非一个完整的、可编译的安卓项目。**

本代码旨在用于技术交流与学习，它省略了所有与 Android 框架、UI 绑定和网络请求相关的具体实现细节。其目的是清晰地展示“普通模式”下，通过二分法结合游戏内命令反馈来定位目标的后端逻辑流程。

## 获取完整版 (Get the full version)
点击链接加入群聊【encore tool】：https://qm.qq.com/q/F8RW0aZHZm

## 核心思路 (Core Concept)

本算法的核心是通过向游戏发送带有坐标范围限制的查询指令，并根据指令是否“有效”的反馈，来逐步缩小目标的可能存在区域。

其主要流程分为以下几个阶段：

1.  **维度检查**: 确认目标是否在当前可通信的维度。
2.  **象限判断**: 分别确定目标在 X 轴和 Z 轴的正/负半轴，将搜索空间缩小到四分之一。
3.  **二分搜索**:
    *   首先对 X 轴的范围进行二分法迭代搜索，直至确定精确的 X 坐标。
    *   然后，在定位 Z 轴时，使用已确定的 X 坐标，并附加一个**容错半径**（例如 `x = finalX - radius, dx = radius * 2`），以兼容目标在 Z 轴定位过程中的小范围移动。
    *   最后对 Z 轴的范围进行二分法迭代搜索，直至确定最终坐标。

## 代码结构

-   `src/LocatorCoreModule.kt`: 包含了定位算法核心的状态机和逻辑流程的伪代码。
-   `src/MainInteractionController.kt`: 展示了 UI 控制器如何与核心模块进行交互的思路。

## 许可证 (License)

本项目采用  开源许可证。

---

*This repository is for conceptual demonstration only. For the full application, please check the official channels.*
