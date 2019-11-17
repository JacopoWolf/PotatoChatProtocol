<center>

<h1> 

***Potato Chat Protocol*** 

:potato: :potato: :potato:

</h1>

![](https://img.shields.io/badge/warning-School%20Project-important?style=for-the-badge)
![](https://img.shields.io/github/license/JacopoWolf/PotatoChatProtocol?style=for-the-badge)

![Maintenance](https://img.shields.io/maintenance/yes/2020?style=for-the-badge)
![](https://img.shields.io/github/commit-activity/m/JacopoWolf/PotatoChatProtocol?color=blueviolet&style=for-the-badge)

[![](https://img.shields.io/jitpack/v/github/jacopowolf/potatochatprotocol?label=Jitpack%20latest%20release&style=for-the-badge)](https://jitpack.io/#JacopoWolf/PotatoChatProtocol)
![JitPack - Downloads](https://img.shields.io/jitpack/dm/github/jacopowolf/potatochatprotocol?color=darkblue&style=for-the-badge)



</center>

---

Read protocol's [general description](docs/PCP.md) and original idea or go see all the [versions](docs/README.md) and their implementations.

---

## Style rules

### Branch naming
* `master` is for documentation only. Protected.
* `dev-{M version}` is for major versions. Those are tagged for releases. Protected.
* `plan-{M version}-{scope}` are for planning and designing base classes.
* `feature-{M version}-{feature name}` are for developing a feature for the dev-* branch. Can contain documentation.
* `docs-{M version}` are for writing **only** additional documentation.
  
names like `USERNAME-master-feature1` are **NOT** accepted.

## Download
> visit [JitPack](https://jitpack.io/#JacopoWolf/PotatoChatProtocol)

if you're using maven add this to your **pom.xml**

```xml
<repositories>

	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>

</repositories>


<dependencies>

	<dependency>
		<groupId>com.github.JacopoWolf</groupId>
		<artifactId>PotatoChatProtocol</artifactId>
		<version>Tag</version>
	</dependency>

</dependencies>
```

