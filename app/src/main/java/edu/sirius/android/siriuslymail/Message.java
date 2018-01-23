package edu.sirius.android.siriuslymail;

import java.io.Serializable;

public class Message implements Serializable{
    public String from, to, subject, body, copy, folder;
    public Long id;
}
