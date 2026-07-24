/**
 * Shared kernel: Money, the domain exception hierarchy and transverse REST/
 * config plumbing. Open module — every business module may use any of it, and
 * it depends on none of them.
 */
@org.springframework.modulith.ApplicationModule(type = org.springframework.modulith.ApplicationModule.Type.OPEN)
package com.trinity.common;
