package iceberg.util;

import java.util.Locale;

public final class Strings {
    private Strings() {
        // No instances
    }
    
    public static String chop(String s) {
        if (s == null) return null;
        if (s.length() == 0) return s;
        int len = s.length();
        if (len == 1) return "";
        return s.substring(0, len - 1);
    }
    
    public static String capitalize(String s) {
        return capitalize(s, Locale.getDefault());
    }

    public static String capitalize(String s, Locale locale) {
        if (s == null) {
            return null;
        }

        switch (s.length()) {
            case 0:
                return s;
            case 1:
                return s.toUpperCase(locale);
            default:
                return s.substring(0, 1).toUpperCase(locale)
                        + s.substring(1);
        }
    }
    
    public static String stringOf(char c, int count) {
        String s = "";
        for (int i = 0; i < count; i++) {
            s += c;
        }

        return s;
    }

    public static String stringOf(String str, int count) {
        String s = "";
        for (int i = 0; i < count; i++) {
            s += str;
        }

        return s;
    }

    public static String padLeft(String s, char fill, int len) {
        String rv = s;
        for (int i = 0; i < len - s.length(); i++) {
            rv = fill + rv;
        }

        return rv;
    }

    public static String padLeft(String s, int len) {
        return padLeft(s, ' ', len);
    }

    public static String padRight(String s, char fill, int len) {
        String rv = s;
        for (int i = 0; i < len - s.length(); i++) {
            rv = rv + fill;
        }

        return rv;
    }

    public static String padRight(String s, int len) {
        return padRight(s, ' ', len);
    }

    public static String join(Iterable<?> words) {
     return join(words, " ");
    }
    
    public static String join(Iterable<?> words, char sep) {
     return join(words, String.valueOf(sep));
    }
    
    public static String join(Iterable<?> words, String sep) {
     StringBuilder buffer = new StringBuilder();
     boolean first = true;
     for (Object w: words) {
     if (first) {
     first = false;
     } else {
     buffer.append(sep);
     }
     buffer.append(w);
     }
    
     return buffer.toString();
    }
    
    public static String join(Object[] words) {
        return join(words, " ");
    }

    public static String join(Object[] words, int start) {
        return join(words, " ", start);
    }

    public static String join(Object[] words, int start, int end) {
        return join(words, " ", start, end);
    }

    public static String join(Object[] words, char sep) {
        return join(words, String.valueOf(sep));
    }

    public static String join(Object[] words, char sep, int start) {
        return join(words, String.valueOf(sep), start);
    }

    public static String join(Object[] words, char sep, int start, int end) {
        return join(words, String.valueOf(sep), start, end);
    }

    public static String join(Object[] words, String sep) {
        return join(words, sep, 0, (words != null ? words.length : -1));
    }
    
    public static String join(Object[] words, String sep, int start) {
        return join(words, sep, start, (words != null ? words.length : -1));
    }

    public static String join(Object[] words, String sep, int start, int end) {
        if (words != null && start >= 0 && end <= words.length && start <= end) {
         StringBuilder rv = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (i != start) {
                    rv.append(sep);
                }
                rv.append(words[i]);
            }

            return rv.toString();
        } else {
            return null;
        }
    }
    
    // Regular expressions
    
    public static String escapeForRegexp(char c) {
        if (!Character.isLetterOrDigit(c)) {
            switch (c) {
                case '$':
                case '^':
                case '\\':
                case '*':
                case '+':
                case '?':
                case '|':
                case '[':
                case ']':
                case '{':
                case '}':
                case '(':
                case ')':
                case '.':
                    return "\\" + c;
                default:
                    return String.valueOf(c);
            }
        }

        return String.valueOf(c);
    }

    public static String escapeForRegexp(String pat) {
        if (pat == null) {
            return null;
        }

        StringBuffer res = new StringBuffer();
        for (int i = 0; i < pat.length(); i++) {
            res.append(escapeForRegexp(pat.charAt(i)));
        }

        return res.toString();
    }
}

