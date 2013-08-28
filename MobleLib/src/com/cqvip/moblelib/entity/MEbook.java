package com.cqvip.moblelib.entity;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

public class MEbook extends Model{
	// 正在下载
	public final static int TYPE_ON_DOWNLOADING = 0;
	// 下载完成
	public final static int TYPE_DOWNLOADED = 1;
	private Integer id;//自增id
	private String lngid;//电子书id
	private String years;//年
	private String num;//第几期
	private String title_c;//标题
	private String name_c;//来源书籍
	private String remark_c;//简述
	private String writer;//用户
	private int pagecount;//页数
	private int pdfsize;//大小
	private String imgurl;//图片
	private String beginpage;//开始页
	private String endpage;//结束
	private Long downloadid;//下载id
	
	private Integer isdownload;//0 下载中，1是下载完成

	public MEbook() {
		super(new PrimaryKey("id",true));
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public void setLngid(String lngid) {
		this.lngid = lngid;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public void setTitle_c(String title_c) {
		this.title_c = title_c;
	}

	public void setName_c(String name_c) {
		this.name_c = name_c;
	}

	public void setRemark_c(String remark_c) {
		this.remark_c = remark_c;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public void setPdfsize(int pdfsize) {
		this.pdfsize = pdfsize;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public void setBeginpage(String beginpage) {
		this.beginpage = beginpage;
	}

	public void setEndpage(String endpage) {
		this.endpage = endpage;
	}

	public Integer getIsdownload() {
		return isdownload;
	}
	public void setDownloadid(Long downloadid) {
		this.downloadid = downloadid;
	}

	public void setIsdownload(Integer isdownload) {
		this.isdownload = isdownload;
	}

	

	public Integer getId() {
		return id;
	}



	public String getLngid() {
		return lngid;
	}



	public String getYears() {
		return years;
	}



	public String getNum() {
		return num;
	}



	public String getTitle_c() {
		return title_c;
	}



	public String getName_c() {
		return name_c;
	}



	public String getRemark_c() {
		return remark_c;
	}



	public String getWriter() {
		return writer;
	}



	public int getPagecount() {
		return pagecount;
	}



	public int getPdfsize() {
		return pdfsize;
	}



	public String getImgurl() {
		return imgurl;
	}



	public String getBeginpage() {
		return beginpage;
	}



	public String getEndpage() {
		return endpage;
	}



	public Long getDownloadid() {
		return downloadid;
	}
	
	
}
