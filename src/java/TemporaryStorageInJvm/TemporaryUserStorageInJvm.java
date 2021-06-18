package TemporaryStorageInJvm;


import java.util.LinkedHashMap;
import java.util.Map;

public class TemporaryUserStorageInJvm 
{
    static Map<String,UserCreation> users = new LinkedHashMap<>();
    public static void add(String email,UserCreation e)
    {
        users.put(email,e);
    }
    public static UserCreation get(String email)
    {
        return users.get(email);
    }
    public static void remove(String email)
    {
        users.remove(email);
    }
    public static Map<String,UserCreation> getall() 
    {
         return users;
    } 
}