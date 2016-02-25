package com.tr.ui.organization.model.cusandorg;

import java.util.List;
/*
 * 组织客户列表返回json对应的一级javaBean
 */
public class CusAndOrg_Page {
     public int index;
     public int size;
     public int total;
     public int type;
     public List<PageItem> listResults;
}
