svn cleanup
svn update

//�滻setting.gradle��ע�͵�module project
sed  -i "s/app'\/\*/app'/g"  ..\\settings.gradle
sed  -i "s/util'\*\//util'/g"  ..\\settings.gradle

gradle clean assembleRelease

//ע��setting.gradle�е�module project
sed  -i "s/app'/app'\/\*/g"  ..\\settings.gradle
sed  -i "s/util'/util'\*\//g"  ..\\settings.gradle