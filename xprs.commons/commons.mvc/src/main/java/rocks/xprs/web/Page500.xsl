<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : Page500.xsl
    Created on : 7. November 2019, 18:35
    Author     : borowski
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html"/>

  <!-- TODO customize transformation rules 
       syntax recommendation http://www.w3.org/TR/xslt 
  -->
  <xsl:template match="/">
    <html>
      <head>
        <title>500 - Internal Server Error</title>
        <meta charset="UTF-8" />
          <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      </head>
      <body>
        <h1>Internal Server Error</h1>
        <p>There was an error during processing that page. Please check the error logs of your server.</p>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
