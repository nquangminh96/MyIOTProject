package com.example.quangminh.myiotproject.Model;

public class LoaiThietBi {
    private String loai;
    private String ten;

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public LoaiThietBi(String loai, String ten) {

        this.loai = loai;
        this.ten = ten;
    }
}
