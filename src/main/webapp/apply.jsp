<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>E-Visa Portal</title>
    <style>
        body { font-family: Arial, sans-serif; background-color: #f5f5dc; color: #483c32; display: grid; place-items: center; height: 100vh; margin: 0; }
        .form-container { background-color: #ffffff; padding: 40px; border: 1px solid #d3cfc8; box-shadow: 0 4px 6px rgba(0,0,0,0.05); width: 100%; max-width: 400px; }
        h2 { margin-top: 0; text-align: center; letter-spacing: 1px; }
        .input-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; font-weight: bold; }
        input { width: 100%; padding: 10px; border: 1px solid #ccc; background-color: #faf9f6; box-sizing: border-box; }
        button { width: 100%; background-color: #483c32; color: #ffffff; padding: 12px; border: none; cursor: pointer; font-weight: bold; margin-top: 10px; }
        button:hover { background-color: #2a231d; }
    </style>
</head>
<body>
    <div class="form-container">
        <h2>E-VISA APPLICATION</h2>
        <form action="ApplicationController" method="POST">
            <div class="input-group">
                <label>Full Name</label>
                <input type="text" name="fullName" required>
            </div>
            <div class="input-group">
                <label>Passport Number</label>
                <input type="text" name="passportNum" required>
            </div>
            <div class="input-group">
                <label>Nationality</label>
                <input type="text" name="nationality" required>
            </div>
            <button type="submit">SUBMIT SECURELY</button>
        </form>
    </div>
</body>
</html>