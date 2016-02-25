package com.tr.model;

import java.io.Serializable;
/**
 * im聊天消息的消息体
 * @author Administrator
 *
 */
public class IMChatMsg  implements Serializable
{

    private static final long serialVersionUID = 327645985790789086L;

    private String id;
    private String name;
    private String image;
    private String subCategoryName;
    private String costPerPerson;
    private String address;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }
    
    public String getSubCategoryName()
    {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName)
    {
        this.subCategoryName = subCategoryName;
    }

    public String getCostPerPerson()
    {
        return costPerPerson;
    }

    public void setCostPerPerson(String costPerPerson)
    {
        this.costPerPerson = costPerPerson;
    }
    
    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}
