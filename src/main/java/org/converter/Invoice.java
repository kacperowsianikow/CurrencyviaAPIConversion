package org.converter;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "faktura")
@XmlAccessorType(XmlAccessType.FIELD)
public class Invoice {
    @XmlElement(name = "komputer")
    private List<Computer> list;

    public Invoice() {}
    public Invoice(List<Computer> list) {
        super();
        this.list = list;
    }

    public List<Computer> getList() {
        return list;
    }

    public void setList(List<Computer> list) {
        this.list = list;
    }
}