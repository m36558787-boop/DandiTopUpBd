<!DOCTYPE html>
<html lang="bn">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>DENDI TOP UP BD</title>
<style>
*{box-sizing:border-box}
body{
 margin:0;background:#0b132b;color:white;
 font-family:Arial,sans-serif;padding-bottom:30px
}
header{
 background:#182848;padding:20px;text-align:center;
 border-bottom:2px solid #00e5ff
}
header h2{color:#00e5ff;margin:0}
.container{padding:16px;max-width:500px;margin:auto}
.banner{
 background:linear-gradient(135deg,#162447,#2455a4);
 padding:22px;border-radius:15px;text-align:center;
 margin-bottom:20px
}
h3{color:#00e5ff}
input,select{
 width:100%;padding:13px;margin:8px 0;
 border:1px solid #00e5ff;border-radius:8px;
 background:#182848;color:white;font-size:16px
}
.packages{
 display:grid;grid-template-columns:1fr 1fr;gap:12px
}
.package{
 background:#182848;border:2px solid #34456b;
 border-radius:12px;padding:16px 8px;
 text-align:center;cursor:pointer
}
.package.selected{border-color:#00e5ff;background:#203d64}
.package strong{display:block;margin-bottom:8px}
.price{color:#00ffae;font-weight:bold}
button{
 width:100%;padding:14px;border:0;border-radius:9px;
 background:#00e5ff;color:#071526;font-weight:bold;
 font-size:16px;cursor:pointer;margin-top:12px
}
.note{color:#b8c7df;font-size:13px;line-height:1.6}
</style>
</head>
<body>
<header>
<h2>💎 DENDI TOP UP BD</h2>
<p>Free Fire Diamond Top Up</p>
</header>

<div class="container">
<div class="banner">
<h2>🔥 Diamond Top Up</h2>
<p>আপনার পছন্দের প্যাকেজ বেছে নিন</p>
</div>

<h3>💎 Diamond Package</h3>
<div class="packages" id="packages"></div>

<h3>🎮 Player Information</h3>
<input id="uid" placeholder="Free Fire Player UID লিখুন">

<h3>💳 Payment Method</h3>
<select id="payment">
<option value="bKash">bKash</option>
<option value="Nagad">Nagad</option>
</select>

<p class="note">পেমেন্ট করার আগে অ্যাডমিনের কাছ থেকে সঠিক পেমেন্ট নম্বর নিশ্চিত করুন।</p>
<input id="trx" placeholder="Transaction ID (TrxID)">

<button onclick="orderNow()">অর্ডার করুন</button>

<p class="note" id="result"></p>
</div>

<script>
const products = [
 {diamonds:25,price:25},
 {diamonds:50,price:40},
 {diamonds:100,price:75},
 {diamonds:210,price:150},
 {diamonds:530,price:370},
 {diamonds:1080,price:740}
];

let selected = products[0];
const area = document.getElementById('packages');

products.forEach((p,i)=>{
 const el=document.createElement('div');
 el.className='package'+(i===0?' selected':'');
 el.innerHTML='<strong>💎 '+p.diamonds+
 ' Diamonds</strong><span class="price">৳'+p.price+'</span>';
 el.onclick=()=>{
  selected=p;
  document.querySelectorAll('.package').forEach(x=>
   x.classList.remove('selected'));
  el.classList.add('selected');
 };
 area.appendChild(el);
});

function orderNow(){
 const uid=document.getElementById('uid').value.trim();
 const payment=document.getElementById('payment').value;
 const trx=document.getElementById('trx').value.trim();

 if(!uid || !trx){
  alert('Player UID এবং TrxID লিখুন');
  return;
 }

 const order={
  shop:'DENDI TOP UP BD',
  uid:uid,
  diamonds:selected.diamonds,
  price:selected.price,
  payment:payment,
  trx:trx,
  status:'Pending'
 };

 document.getElementById('result').textContent=
 'অর্ডার তথ্য তৈরি হয়েছে। Diamond: '+
 order.diamonds+' | মূল্য: ৳'+order.price+
 ' | অবস্থা: Pending। অ্যাডমিনের অনুমোদন প্রয়োজন।';

 alert('ডেমো অর্ডার তৈরি হয়েছে। বাস্তবে জমা হয়নি।');
}
</script>
</body>
</html>
