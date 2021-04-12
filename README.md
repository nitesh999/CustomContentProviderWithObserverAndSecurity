# CustomContentProviderWithObserverAndSecurity
CustomContentProvider with added observer to listen underlying data changes and security to filter the intended receiver app to share the data and not with all.
1. custom permission for read write
2. granturipermission for sharing  data with only intended apps and not with others
3. granturipermission using intent to share data only once
4. share data with only same signatured apps
5. loader wo listen data changes accross apps
