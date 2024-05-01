<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="html" encoding="UTF-8" indent="yes"/>

    <xsl:template match="/sala_de_fitness">
        <html>
            <head>
                <title>Sala de Fitness</title>
                <style>
                    table, th, td {
                    border: 1px solid black;
                    border-collapse: collapse;
                    }
                    th, td {
                    padding: 8px;
                    text-align: left;
                    }
                </style>
            </head>
            <body>
                <h2>Membrii Sălii de Fitness</h2>
                <table>
                    <tr>
                        <th>ID</th>
                        <th>Nume</th>
                        <th>Telefon</th>
                        <th>Email</th>
                        <th>Tip Abonament</th>
                        <th>Început Abonament</th>
                        <th>Sfârșit Abonament</th>
                    </tr>
                    <xsl:apply-templates select="membri/membru"/>
                </table>

                <h2>Clasele Disponibile</h2>
                <table>
                    <tr>
                        <th>ID Clasă</th>
                        <th>Titlu</th>
                        <th>Instructor</th>
                        <th>Data</th>
                        <th>Ora</th>
                        <th>Locație</th>
                        <th>Capacitate Maximă</th>
                    </tr>
                    <xsl:apply-templates select="clase/clasa"/>
                </table>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="membru">
        <tr>
            <td><xsl:value-of select="@id"/></td>
            <td><xsl:value-of select="nume"/></td>
            <td><xsl:value-of select="contact/telefon"/></td>
            <td><xsl:value-of select="contact/email"/></td>
            <td><xsl:value-of select="abonament/@tip"/></td>
            <td><xsl:value-of select="abonament/@data_inceput"/></td>
            <td><xsl:value-of select="abonament/@data_sfarsit"/></td>
        </tr>
    </xsl:template>

    <xsl:template match="clasa">
        <tr>
            <td><xsl:value-of select="@id"/></td>
            <td><xsl:value-of select="titlu"/></td>
            <td><xsl:value-of select="instructor"/></td>
            <td><xsl:value-of select="programare/@data"/></td>
            <td><xsl:value-of select="programare/@ora"/></td>
            <td><xsl:value-of select="locatie"/></td>
            <td><xsl:value-of select="capacitate_maxima"/></td>
        </tr>
    </xsl:template>

</xsl:stylesheet>
