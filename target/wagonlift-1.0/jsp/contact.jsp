<html>
<head>
<script language="javascript">

function Validate() {
    var name=document.getElementById('name');
    var email = document.getElementById('email');
document.getElementById('msg').innerHTML = "";
var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;

    
if (name.value.length == 0)

{
document.getElementById('msg').innerHTML = "*Please provide name";
  
}
if (!filter.test(email.value)) {
    document.getElementById('msg').innerHTML+= "<br>*Please provide valid email address";


    
 }

}</script>
<style>
 img{
display:block;
margin:0 auto;
}
.content
{
line-height:48px;
color:#625B52;
font-family:tempus sans itc;
width:400px;
margin: 0 auto;
display:block;
text-align:left;
}
.title
{
font-size:72px;
color:#625B52;
float:left;
width:60%;
padding-top:20px;
text-align:right;
font-family:fantasy;
}
.subtitle
{
font-size:20px;
color:#625B52;
float:left;
width:38%;
padding-top:65px;
font-family:fantasy;
}
.top
{
width:100%;
height:100px;
background-color:#E4E2DC;
color:#625B52;
}
.bottom
{
height:50px;
width:100%;
background-color:#E4E2DC;
color:#625B52;
padding-top:20px;
text-align:center;
}
.btn {
  -webkit-border-radius: 30;
  -moz-border-radius: 30;
  border-radius: 26px;
  font-family: Georgia;
  color: #F9F8F5;
  font-size: 20px;
  background: #625B52;
  padding: 5px 10px 5px 10px;
  border: solid #161a13 2px;
  text-decoration: none;
margin:auto;
width:160px;
height:40px;

}
.btn:hover {
  background: #E4E2DC;
  text-decoration: none;
}
.contact_form
{

background-color:#F9F8F5;
width:100%;
text-align:center;
}
.input
{
width:400px;
height:40px;
}
.inputarea
{width:400px;
height:120px;
}
#msg
{
color:red;
font-family:arial;
font-size:20px;
}
</style>
</head>
<body>
<div class="top">
<div class="title">Notify Me Details</div>

<div class ="subtitle">&nbsp;&nbsp;&nbsp;Wagon Lift</div></div>
<div class="contact_form"><div class="content" style="font-size:24px">
<form action="notifyme" method="post">
<div id="msg" style="display: inline;">${message}</div>
<br/>
Name:<b style="color:red">*</b><br><input type="text" name="name" class="input"><br>
Email:<b style="color:red">*</b><br><input type="text" name="email" class="input"><br>
Subject: <br><input type="text" name="subject" class="input"><br>
Message: <br><input type="textarea" name="message" class="inputarea"><br>

<br>
<input type="submit" value="Submit" class="btn" onClick="Validate(); return false;"></input>

</form> 
<br/><br/>                                                                                
</div>
</div>
<div class="bottom">Copyright Wagonlift. All rights reserved.</div>
</body>

</html>