find "<error" checkstyle-results\checkstyle_report.xml || goto success

goto error

:error

exit 1

:success
