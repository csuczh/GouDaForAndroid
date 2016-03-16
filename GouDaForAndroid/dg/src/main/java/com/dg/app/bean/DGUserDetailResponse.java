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
@XStreamAlias("gouda")
public class DGUserDetailResponse extends Entity{

    @XStreamAlias("code")
    private int code; //错误码 0为成功

    @XStreamAlias("msg")
    private String msg;//错误或成功信息

    @XStreamAlias("user_detail")
    private UserDetail userDetail;

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

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    @XStreamAlias("userdetail")
    public class UserDetail implements Serializable{

        @XStreamAlias("nickname")
        private String nickname;//用户昵称

        @XStreamAlias("age")
        private int age;//用户年龄
        
        @XStreamAlias("career")
        private String career;//职业

        @XStreamAlias("sex")
        private int sex;//用户性别 性别0男 1女
        
        @XStreamAlias("province")
        private String province;//用户所处的省份
        
        @XStreamAlias("city_name")
        private String city_name;//用户所处的城市名称

        @XStreamAlias("logo")
        private String logo;//用户头像url

        @XStreamAlias("tags")
        private List<Tag> tags = new ArrayList<>();

        @XStreamAlias("imgList")
        private ImgList imgList;

        @XStreamAlias("fans_num")
        private int fans_num;//粉丝数

        @XStreamAlias("follow_num")
        private int follow_num;//关注数

        @XStreamAlias("grade")
        private int grade;//积分

        @XStreamAlias("gradeTag")
        private String gradeTag;//积分称号

        @XStreamAlias("easemob_id")
        private String easemob_id;//被检索用户环信账号

        @XStreamAlias("have_dog")
        private int have_dog;//被检索用户是否有狗0为无1为有

        @XStreamAlias("is_fan")
        private int is_fan;//查看用户是否是被检索用户的粉丝0不是1是

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getCareer() {
            return career;
        }

        public void setCareer(String career) {
            this.career = career;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public ImgList getImgList() {
			return imgList;
		}

		public void setImgList(ImgList imgList) {
			this.imgList = imgList;
		}

        public int getFans_num() {
            return fans_num;
        }

        public void setFans_num(int fans_num) {
            this.fans_num = fans_num;
        }

        public int getFollow_num() {
            return follow_num;
        }

        public void setFollow_num(int follow_num) {
            this.follow_num = follow_num;
        }

        public int getGrade() {
            return grade;
        }

        public void setGrade(int grade) {
            this.grade = grade;
        }

        public String getGradeTag() {
            return gradeTag;
        }

        public void setGradeTag(String gradeTag) {
            this.gradeTag = gradeTag;
        }

        public String getEasemob_id() {
            return easemob_id;
        }

        public void setEasemob_id(String easemob_id) {
            this.easemob_id = easemob_id;
        }

        public int getHave_dog() {
            return have_dog;
        }

        public void setHave_dog(int have_dog) {
            this.have_dog = have_dog;
        }

        public int getIs_fan() {
            return is_fan;
        }

        public void setIs_fan(int is_fan) {
            this.is_fan = is_fan;
        }

        @Override
        public String toString() {
            return "UserDetail{" +
                    "nickname='" + nickname + '\'' +
                    ", age=" + age +
                    ", career='" + career + '\'' +
                    ", sex=" + sex +
                    ", province='" + province + '\'' +
                    ", city_name='" + city_name + '\'' +
                    ", logo='" + logo + '\'' +
                    ", tags=" + tags +
                    ", imgList=" + imgList +
                    ", fans_num=" + fans_num +
                    ", follow_num=" + follow_num +
                    ", grade=" + grade +
                    ", gradeTag='" + gradeTag + '\'' +
                    ", easemob_id='" + easemob_id + '\'' +
                    ", have_dog=" + have_dog +
                    ", is_fan=" + is_fan +
                    '}';
        }
    }

    
    @Override
	public String toString() {
		return "DGUserDetailResponse [code=" + code + ", msg=" + msg
				+ ", userDetail=" + userDetail.toString() + "]";
	}

    @XStreamAlias("imglist")
    public class ImgList implements Serializable{
    	
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
            return "ImgList{" +
                    "imgList=" + imgList +
                    '}';
        }
    }
    
}
