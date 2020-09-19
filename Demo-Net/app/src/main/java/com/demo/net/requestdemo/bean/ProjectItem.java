package com.demo.net.requestdemo.bean;

import java.util.List;

public class ProjectItem {


    /*
     * data : {
     *      "curPage":1,
     *      "datas":[
     *          {"apkLink":"","author":"lulululbj","chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"Github 上关于 Wanandroid 的客户端也层出不穷，Java的，Kotlin 的，Flutter 的，Mvp 的，MVMM 的，各种各样，但是还没看到 Kotlin+MVVM+LiveData+协程 版本的，加上最近正在看 MVVM 和 LiveData，就着手把我之前写的 Mvp 版本的 Wanandroid 改造成 MVVM + Kotlin + LiveData + 协程 版本。","envelopePic":"https://wanandroid.com/blogimgs/54f4350f-039d-48b6-a38b-0933e1405004.png","fresh":false,"id":8273,"link":"http://www.wanandroid.com/blog/show/2554","niceDate":"2019-04-18","origin":"","prefix":"","projectLink":"https://github.com/lulululbj/wanandroid/tree/mvvm-kotlin","publishTime":1555593015000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"真香！Kotlin+MVVM+LiveData+协程 打造 Wanandroid！","type":0,"userId":-1,"visible":1,"zan":0},
     *          {"apkLink":"","author":"OnexZgj","chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"该应用程序是玩Android部分api和干货网站部分api的flutter版本的技术类文章查看APP。\r\n主要功能包括：首页、项目、公众号、搜索等。","envelopePic":"https://wanandroid.com/blogimgs/4681d6c0-0d76-4c69-a866-7ad66dde10cd.png","fresh":false,"id":8269,"link":"http://www.wanandroid.com/blog/show/2550","niceDate":"2019-04-18","origin":"","prefix":"","projectLink":"https://github.com/OnexZgj/flutter_onex","publishTime":1555592366000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"是时候体验一波Flutter啦","type":0,"userId":-1,"visible":1,"zan":0},
     *          {"apkLink":"","author":"dlgchg","chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"使用flutter开发的github客户端","envelopePic":"https://wanandroid.com/blogimgs/af4530e7-f244-4b3f-b278-9be41044e811.png","fresh":false,"id":8268,"link":"http://www.wanandroid.com/blog/show/2549","niceDate":"2019-04-18","origin":"","prefix":"","projectLink":"https://github.com/dlgchg/flutter_github","publishTime":1555592326000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"使用flutter开发的github客户端","type":0,"userId":-1,"visible":0,"zan":0},
     *          ......
     *      ],
     *      "offset":0,
     *      "over":false,
     *      "pageCount":9,
     *      "size":15,
     *      "total":135
     * }
     * errorCode : 0
     * errorMsg :
     */

    private DataBean data;
    private int errorCode;
    private String errorMsg;

    @Override
    public String toString() {
        return "ProjectItem{" +
                "data=" + data +
                ", errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        /*
         * curPage : 1
         * datas : [{"apkLink":"","author":"lulululbj","chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"Github 上关于 Wanandroid 的客户端也层出不穷，Java的，Kotlin 的，Flutter 的，Mvp 的，MVMM 的，各种各样，但是还没看到 Kotlin+MVVM+LiveData+协程 版本的，加上最近正在看 MVVM 和 LiveData，就着手把我之前写的 Mvp 版本的 Wanandroid 改造成 MVVM + Kotlin + LiveData + 协程 版本。","envelopePic":"https://wanandroid.com/blogimgs/54f4350f-039d-48b6-a38b-0933e1405004.png","fresh":false,"id":8273,"link":"http://www.wanandroid.com/blog/show/2554","niceDate":"2019-04-18","origin":"","prefix":"","projectLink":"https://github.com/lulululbj/wanandroid/tree/mvvm-kotlin","publishTime":1555593015000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"真香！Kotlin+MVVM+LiveData+协程 打造 Wanandroid！","type":0,"userId":-1,"visible":1,"zan":0},{"apkLink":"","author":"OnexZgj","chapterId":294,"chapterName":"完整项目","collect":false,"courseId":13,"desc":"该应用程序是玩Android部分api和干货网站部分api的flutter版本的技术类文章查看APP。\r\n主要功能包括：首页、项目、公众号、搜索等。","envelopePic":"https://wanandroid.com/blogimgs/4681d6c0-0d76-4c69-a866-7ad66dde10cd.png","fresh":false,"id":8269,"link":"http://www.wanandroid.com/blog/show/2550","niceDate":"2019-04-18","origin":"","prefix":"","projectLink":"https://github.com/OnexZgj/flutter_onex","publishTime":1555592366000,"superChapterId":294,"superChapterName":"开源项目主Tab","tags":[{"name":"项目","url":"/project/list/1?cid=294"}],"title":"是时候体验一波Flutter啦","type":0,"userId":-1,"visible":1,"zan":0},......]
         * offset : 0
         * over : false
         * pageCount : 9
         * size : 15
         * total : 135
         */

        private int curPage;
        private int offset;
        private boolean over;
        private int pageCount;
        private int size;
        private int total;
        private List<DatasBean> datas;

        @Override
        public String toString() {
            return "DataBean{" +
                    "curPage=" + curPage +
                    ", offset=" + offset +
                    ", over=" + over +
                    ", pageCount=" + pageCount +
                    ", size=" + size +
                    ", total=" + total +
                    ", datas=" + datas +
                    '}';
        }

        public int getCurPage() {
            return curPage;
        }

        public void setCurPage(int curPage) {
            this.curPage = curPage;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public boolean isOver() {
            return over;
        }

        public void setOver(boolean over) {
            this.over = over;
        }

        public int getPageCount() {
            return pageCount;
        }

        public void setPageCount(int pageCount) {
            this.pageCount = pageCount;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<DatasBean> getDatas() {
            return datas;
        }

        public void setDatas(List<DatasBean> datas) {
            this.datas = datas;
        }

        public static class DatasBean {
            /**
             * apkLink :
             * author : lulululbj
             * chapterId : 294
             * chapterName : 完整项目
             * collect : false
             * courseId : 13
             * desc : Github 上关于 Wanandroid 的客户端也层出不穷，Java的，Kotlin 的，Flutter 的，Mvp 的，MVMM 的，各种各样，但是还没看到 Kotlin+MVVM+LiveData+协程 版本的，加上最近正在看 MVVM 和 LiveData，就着手把我之前写的 Mvp 版本的 Wanandroid 改造成 MVVM + Kotlin + LiveData + 协程 版本。
             * envelopePic : https://wanandroid.com/blogimgs/54f4350f-039d-48b6-a38b-0933e1405004.png
             * fresh : false
             * id : 8273
             * link : http://www.wanandroid.com/blog/show/2554
             * niceDate : 2019-04-18
             * origin :
             * prefix :
             * projectLink : https://github.com/lulululbj/wanandroid/tree/mvvm-kotlin
             * publishTime : 1555593015000
             * superChapterId : 294
             * superChapterName : 开源项目主Tab
             * tags : [{"name":"项目","url":"/project/list/1?cid=294"}]
             * title : 真香！Kotlin+MVVM+LiveData+协程 打造 Wanandroid！
             * type : 0
             * userId : -1
             * visible : 1
             * zan : 0
             */

            private String apkLink;
            private String author;
            private int chapterId;
            private String chapterName;
            private boolean collect;
            private int courseId;
            private String desc;
            private String envelopePic;
            private boolean fresh;
            private int id;
            private String link;
            private String niceDate;
            private String origin;
            private String prefix;
            private String projectLink;
            private long publishTime;
            private int superChapterId;
            private String superChapterName;
            private String title;
            private int type;
            private int userId;
            private int visible;
            private int zan;
            private List<TagsBean> tags;

            @Override
            public String toString() {
                return "DatasBean{" +
                        "apkLink='" + apkLink + '\'' +
                        ", author='" + author + '\'' +
                        ", chapterId=" + chapterId +
                        ", chapterName='" + chapterName + '\'' +
                        ", collect=" + collect +
                        ", courseId=" + courseId +
                        ", desc='" + desc + '\'' +
                        ", envelopePic='" + envelopePic + '\'' +
                        ", fresh=" + fresh +
                        ", id=" + id +
                        ", link='" + link + '\'' +
                        ", niceDate='" + niceDate + '\'' +
                        ", origin='" + origin + '\'' +
                        ", prefix='" + prefix + '\'' +
                        ", projectLink='" + projectLink + '\'' +
                        ", publishTime=" + publishTime +
                        ", superChapterId=" + superChapterId +
                        ", superChapterName='" + superChapterName + '\'' +
                        ", title='" + title + '\'' +
                        ", type=" + type +
                        ", userId=" + userId +
                        ", visible=" + visible +
                        ", zan=" + zan +
                        ", tags=" + tags +
                        '}';
            }

            public String getApkLink() {
                return apkLink;
            }

            public void setApkLink(String apkLink) {
                this.apkLink = apkLink;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }

            public int getChapterId() {
                return chapterId;
            }

            public void setChapterId(int chapterId) {
                this.chapterId = chapterId;
            }

            public String getChapterName() {
                return chapterName;
            }

            public void setChapterName(String chapterName) {
                this.chapterName = chapterName;
            }

            public boolean isCollect() {
                return collect;
            }

            public void setCollect(boolean collect) {
                this.collect = collect;
            }

            public int getCourseId() {
                return courseId;
            }

            public void setCourseId(int courseId) {
                this.courseId = courseId;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getEnvelopePic() {
                return envelopePic;
            }

            public void setEnvelopePic(String envelopePic) {
                this.envelopePic = envelopePic;
            }

            public boolean isFresh() {
                return fresh;
            }

            public void setFresh(boolean fresh) {
                this.fresh = fresh;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }

            public String getNiceDate() {
                return niceDate;
            }

            public void setNiceDate(String niceDate) {
                this.niceDate = niceDate;
            }

            public String getOrigin() {
                return origin;
            }

            public void setOrigin(String origin) {
                this.origin = origin;
            }

            public String getPrefix() {
                return prefix;
            }

            public void setPrefix(String prefix) {
                this.prefix = prefix;
            }

            public String getProjectLink() {
                return projectLink;
            }

            public void setProjectLink(String projectLink) {
                this.projectLink = projectLink;
            }

            public long getPublishTime() {
                return publishTime;
            }

            public void setPublishTime(long publishTime) {
                this.publishTime = publishTime;
            }

            public int getSuperChapterId() {
                return superChapterId;
            }

            public void setSuperChapterId(int superChapterId) {
                this.superChapterId = superChapterId;
            }

            public String getSuperChapterName() {
                return superChapterName;
            }

            public void setSuperChapterName(String superChapterName) {
                this.superChapterName = superChapterName;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getUserId() {
                return userId;
            }

            public void setUserId(int userId) {
                this.userId = userId;
            }

            public int getVisible() {
                return visible;
            }

            public void setVisible(int visible) {
                this.visible = visible;
            }

            public int getZan() {
                return zan;
            }

            public void setZan(int zan) {
                this.zan = zan;
            }

            public List<TagsBean> getTags() {
                return tags;
            }

            public void setTags(List<TagsBean> tags) {
                this.tags = tags;
            }

            public static class TagsBean {
                /**
                 * name : 项目
                 * url : /project/list/1?cid=294
                 */

                private String name;
                private String url;

                @Override
                public String toString() {
                    return "TagsBean{" +
                            "name='" + name + '\'' +
                            ", url='" + url + '\'' +
                            '}';
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }
        }
    }
}
