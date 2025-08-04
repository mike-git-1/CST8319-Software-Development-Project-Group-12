<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Inventra</title>
    <link rel="stylesheet" href="CSS/registerConfirmView.css" />
    <link
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
      rel="stylesheet"
    />

    <script src="JS/registerConfirm.js" defer></script>
  </head>
  <body>
    <div class="form-container">
      <div class="form-header">
        <h1>Thank you for signing up!</h1>
        <p>
          Check your email for a confirmation link to activate your account.
        </p>
        <p class="redirect-message">
          You will be redirected to the home page shortly...
        </p>
      </div>
    </div>

    <script>
      setTimeout(() => {
        window.location.href = "/inventra/index";
      }, 5000);
    </script>
  </body>
</html>
