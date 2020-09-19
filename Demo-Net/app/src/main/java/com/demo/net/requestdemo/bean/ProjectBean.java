package com.demo.net.requestdemo.bean;

import java.util.List;

public class ProjectBean {

    /*
     * data : [
     *      {"children":[],"courseId":13,"id":294,"name":"完整项目","order":145000,"parentChapterId":293,"userControlSetTop":false,"visible":0},
     *      {"children":[],"courseId":13,"id":402,"name":"跨平台应用","order":145001,"parentChapterId":293,"userControlSetTop":false,"visible":1},
     *      {"children":[],"courseId":13,"id":367,"name":"资源聚合类","order":145002,"parentChapterId":293,"userControlSetTop":false,"visible":1},
     *      {"children":[],"courseId":13,"id":323,"name":"动画","order":145003,"parentChapterId":293,"userControlSetTop":false,"visible":1},
     *      {"children":[],"courseId":13,"id":314,"name":"RV列表动效","order":145004,"parentChapterId":293,"userControlSetTop":false,"visible":1},
     *      {"children":[],"courseId":13,"id":358,"name":"项目基础功能","order":145005,"parentChapterId":293,"userControlSetTop":false,"visible":1},
     *      ......
     * ]
     * errorCode : 0
     * errorMsg :
     */

    private int errorCode;
    private String errorMsg;
    private List<DataBean> data;

    @Override
    public String toString() {
        return "ProjectBean{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * children : []
         * courseId : 13
         * id : 294
         * name : 完整项目
         * order : 145000
         * parentChapterId : 293
         * userControlSetTop : false
         * visible : 0
         */

        private int courseId;
        private int id;
        private String name;
        private int order;
        private int parentChapterId;
        private boolean userControlSetTop;
        private int visible;
        private List<?> children;

        @Override
        public String toString() {
            return "DataBean{" +
                    "courseId=" + courseId +
                    ", id=" + id +
                    ", name='" + name + '\'' +
                    ", order=" + order +
                    ", parentChapterId=" + parentChapterId +
                    ", userControlSetTop=" + userControlSetTop +
                    ", visible=" + visible +
                    ", children=" + children +
                    '}';
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getParentChapterId() {
            return parentChapterId;
        }

        public void setParentChapterId(int parentChapterId) {
            this.parentChapterId = parentChapterId;
        }

        public boolean isUserControlSetTop() {
            return userControlSetTop;
        }

        public void setUserControlSetTop(boolean userControlSetTop) {
            this.userControlSetTop = userControlSetTop;
        }

        public int getVisible() {
            return visible;
        }

        public void setVisible(int visible) {
            this.visible = visible;
        }

        public List<?> getChildren() {
            return children;
        }

        public void setChildren(List<?> children) {
            this.children = children;
        }
    }
}
