//groovy scripts/git-head-hash.groovy
println new File('.git/' + new File('.git/HEAD').text.trim().replaceAll('ref: ', '')).text.trim()
