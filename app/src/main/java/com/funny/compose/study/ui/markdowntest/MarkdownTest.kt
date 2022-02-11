package com.funny.compose.study.ui.markdowntest

import androidx.compose.runtime.Composable
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun MarkdownTest() {
    val text = """
        <small>小字体</small>  
        <p>see <a href=\"https://github.com/markdown-it/markdown-it-emoji#change-output\">how to change output</a> with twemoji.</p>\n
        <p>This is <abbr title=\"Hyper Text Markup Language\">HTML</abbr> abbreviation example.</p>
        <font color="Blue">Test</font>  
        <font color=#00ffff size=3>null</font>  
        <span style="font-size: 12px;">Nǐ hǎo</span>  
         
        <strong>词性：</strong>感叹词  
        <strong>相似词汇：</strong>  
        Hello!  你好!;喂!;  
        Hi!  嗨!;你好!;  
        Hallo!  你好!;  
    """.trimIndent()
    MarkdownText(markdown = text)
}