<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : Page404.xsl
    Created on : 7. November 2019, 18:34
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
        <title>404 - File not found</title>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
      </head>
      <body>
        <h1>File not found</h1>
        <p>The page you are looking for doesn't exist or doesn't exist anymore.</p>
      </body>
    </html>
  </xsl:template>

</xsl:stylesheet>
