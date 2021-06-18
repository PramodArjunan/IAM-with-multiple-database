package TemporaryStorageInJvm;

public class CurrentUser
{
    static String email;
    
    public static String getemail()
    {  
        return email;  
    }  
    
    public void setemail(String email) {  
        this.email =email;  
    }   
}
