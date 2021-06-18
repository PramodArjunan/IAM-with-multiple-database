<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<style>
body {font-family: Arial, Helvetica, sans-serif;}
* {box-sizing: border-box}

/* Full-width input fields */
input[type=text], input[type=password] {
  width: 100%;
  padding: 15px;
  margin: 5px 0 22px 0;
  display: inline-block;
  border: none;
  background: #f1f1f1;
}

input[type=text]:focus, input[type=password]:focus {
  background-color: #ddd;
  outline: none;
}

hr {
  border: 1px solid #f1f1f1;
  margin-bottom: 25px;
}

/* Set a style for all buttons */
button {
  background-color: #04AA6D;
  color: white;
  padding: 14px 20px;
  margin: 8px 0;
  border: none;
  cursor: pointer;
  width: 100%;
  opacity: 0.9;
}

button:hover {
  opacity:1;
}

/* Extra styles for the cancel button */
.cancelbtn {
  padding: 14px 20px;
  background-color: #f44336;
}

/* Float cancel and signup buttons and add an equal width */
.cancelbtn, .signupbtn {
  float: left;
  width: 50%;
}

/* Add padding to container elements */
.container {
  padding: 16px;
}

/* Clear floats */
.clearfix::after {
  content: "";
  clear: both;
  display: table;
}

/* Change styles for cancel button and signup button on extra small screens */
@media screen and (max-width: 300px) {
  .cancelbtn, .signupbtn {
     width: 100%;
  }
}
</style>
<body>

<form action="user_creation_and_login" style="border:1px solid #ccc">
     <input type="hidden" name="modalForm" value="usercreation"/>
  <div class="container">
    
    <h1>Sign Up</h1>
    <p>Please fill in this form to create an account.</p>
    <p>Please enter the password given cretiria</p>
    <p>Length of the password should not be less then 8<br>
It should be AplhaNumeric with Mixed Case<br> and one Special character(!#%?><&*) <br></p>
    <hr>
    <label for="name"><b>Name</b></label>
    <input type="text" placeholder="Enter name" name="name" required>
    
    <label for="number"><b>Phone number</b></label>
    <input type="text" placeholder="Enter Phone number" name="number" required>

    <label for="email"><b>Email</b></label>
    <input type="text" placeholder="Enter Email" name="email" required>
    
    <label for="h_id"><b>H_ID</b></label>
    <input type="text" placeholder="Enter H_ID" name="h_id" required>
    
    <label for="rc"><b>Initial RC</b></label>
    <input type="text" placeholder="Enter initial RC" name="rc" required>

    <label for="psw"><b>Password</b></label>
    <input type="password" placeholder="Enter Password" name="psw" required>

    <label for="psw-repeat"><b>Repeat Password</b></label>
    <input type="password" placeholder="Repeat Password" name="psw-repeat" required>
    
    <div class="clearfix">
      
    <button type="submit" class="signupbtn" name="modalForm">Sign Up</button>
    </div>
  </div>
</form>

</body>
</html>

