package com.cqvip.moblelib.entity;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

public class MEbook extends Model{
	// ��������
	public final static int TYPE_ON_DOWNLOADING = 0;
	// �������
	public final static int TYPE_DOWNLOADED = 1;
	private Integer id;//����id
	private String lngid;//������id
	private String years;//��
	private String num;//�ڼ���
	private String title_c;//����
	private String name_c;//��Դ�鼮
	private String remark_c;//����
	private String writer;//�û�
	private int pagecount;//ҳ��
	private int pdfsize;//��С
	private String imgurl;//ͼƬ
	private String beginpage;//��ʼҳ
	private String endpage;//����
	private Long downloadid;//����id
	
	private Integer isdownload;//0 �����У�1���������

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
