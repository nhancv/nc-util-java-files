package task;

import java.util.Date;

/**
 * Created by nhancao on 5/10/17.
 */
public class NTask {

    private String id;
    private String groupId;
    private boolean isActive;
    private int groupPriority;
    private int itemPriority;
    private String itemContent;
    private String updateTime;

    public static NTask build(String id, String groupId, boolean isActive, int groupPriority, int itemPriority, String itemContent) {
        NTask task = new NTask();
        task.setId(id);
        task.setGroupId(groupId);
        task.setActive(isActive);
        task.setGroupPriority(groupPriority);
        task.setItemPriority(itemPriority);
        task.setItemContent(itemContent);
        task.setUpdateTime(new Date().toString());
        return task;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getGroupPriority() {
        return groupPriority;
    }

    public void setGroupPriority(int groupPriority) {
        this.groupPriority = groupPriority;
    }

    public int getItemPriority() {
        return itemPriority;
    }

    public void setItemPriority(int itemPriority) {
        this.itemPriority = itemPriority;
    }

    public String getItemContent() {
        return itemContent;
    }

    public void setItemContent(String itemContent) {
        this.itemContent = itemContent;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "NTask{" +
                "id='" + id + '\'' +
                ", groupId='" + groupId + '\'' +
                ", isActive=" + isActive +
                ", groupPriority=" + groupPriority +
                ", itemPriority=" + itemPriority +
                ", itemContent='" + itemContent + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
