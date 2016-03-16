package com.dg.app.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录用户实体类
 * @version 1.0
 * @created 2012-3-21
 */
@SuppressWarnings("serial")
@XStreamAlias("gouda")
public class DGDogDetailResponse extends Entity{

    @XStreamAlias("code")
    private int code; //错误码 0为成功

    @XStreamAlias("msg")
    private String msg;//错误或成功信息

    @XStreamAlias("dog_detail")
    private DogDetail dogDetail;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DogDetail getDogDetail() {
        return dogDetail;
    }

    public void setDogDetail(DogDetail dogDetail) {
        this.dogDetail = dogDetail;
    }

    @XStreamAlias("dogdetail")
    public class DogDetail implements Serializable {

        @XStreamAlias("dog_nickname")
        private String dog_nickname;//用户昵称

        @XStreamAlias("dog_age")
        private double dog_age;//用户年龄

        @XStreamAlias("dog_variety")
        private String dog_variety;//职业

        @XStreamAlias("dog_sex")
        private int dog_sex;//用户性别 性别0男 1女

        @XStreamAlias("dog_logo")
        private String dog_logo;//用户头像url

        @XStreamAlias("dog_tags")
        private List<Tag> tags = new ArrayList<>();

        @XStreamAlias("dog_imgList")
        private DGImgList dgImgList;

        public String getDog_nickname() {
            return dog_nickname;
        }

        public void setDog_nickname(String dog_nickname) {
            this.dog_nickname = dog_nickname;
        }

        public double getDog_age() {
            return dog_age;
        }

        public void setDog_age(double dog_age) {
            this.dog_age = dog_age;
        }

        public String getDog_variety() {
            return dog_variety;
        }

        public void setDog_variety(String dog_variety) {
            this.dog_variety = dog_variety;
        }

        public int getDog_sex() {
            return dog_sex;
        }

        public void setDog_sex(int dog_sex) {
            this.dog_sex = dog_sex;
        }

        public String getDog_logo() {
            return dog_logo;
        }

        public void setDog_logo(String dog_logo) {
            this.dog_logo = dog_logo;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public DGImgList getDgImgList() {
            return dgImgList;
        }

        public void setDgImgList(DGImgList dgImgList) {
            this.dgImgList = dgImgList;
        }

        @Override
        public String toString() {
            return "DogDetail{" +
                    "dog_nickname='" + dog_nickname + '\'' +
                    ", dog_age=" + dog_age +
                    ", dog_variety='" + dog_variety + '\'' +
                    ", dog_sex=" + dog_sex +
                    ", dog_logo='" + dog_logo + '\'' +
                    ", tags=" + tags +
                    ", dgImgList=" + dgImgList +
                    '}';
        }
    }

    @XStreamAlias("dgimglist")
    public class DGImgList implements Serializable{
    	
    	@XStreamImplicit(itemFieldName="item")
    	private List<String> imgList;

		public List<String> getImgList() {
			return imgList;
		}

		public void setImgList(List<String> imgList) {
			this.imgList = imgList;
		}

        @Override
        public String toString() {
            return "DGImgList{" +
                    "imgList=" + imgList +
                    '}';
        }
    }
    
}
