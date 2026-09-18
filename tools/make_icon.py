"""Generates the AltGen addon icon (128x128) into src/main/resources/assets/altgen/.

Design: purple gradient rounded badge (matches the addon color rgb(130,80,240)),
white account bust, green plus badge = "add account".
Supersampled 4x and downscaled with LANCZOS for crisp edges.
"""
from PIL import Image, ImageDraw

S = 4                      # supersample factor
W = H = 128 * S            # canvas
R = 26 * S                 # badge corner radius

WHITE = (255, 255, 255, 255)
GREEN = (86, 214, 136, 255)
TOP = (150, 108, 252)      # gradient top
BOTTOM = (96, 58, 210)     # gradient bottom

# --- Badge background -------------------------------------------------------
mask = Image.new("L", (W, H), 0)
ImageDraw.Draw(mask).rounded_rectangle([0, 0, W - 1, H - 1], radius=R, fill=255)

grad = Image.new("RGBA", (W, H))
gd = ImageDraw.Draw(grad)
for y in range(H):
    t = y / (H - 1)
    c = tuple(round(TOP[i] + (BOTTOM[i] - TOP[i]) * t) for i in range(3))
    gd.line([(0, y), (W, y)], fill=c + (255,))

img = Image.new("RGBA", (W, H), (0, 0, 0, 0))
img.paste(grad, (0, 0), mask)

# Subtle highlight on the upper half for depth
hl = Image.new("RGBA", (W, H), (0, 0, 0, 0))
ImageDraw.Draw(hl).rounded_rectangle(
    [2 * S, 2 * S, W - 2 * S, H // 2], radius=R, fill=(255, 255, 255, 26)
)
img.alpha_composite(hl)

d = ImageDraw.Draw(img)

# --- Account bust -----------------------------------------------------------
cx, cy, r = 64 * S, 50 * S, 17 * S                 # head
d.ellipse([cx - r, cy - r, cx + r, cy + r], fill=WHITE)
d.ellipse([36 * S, 70 * S, 92 * S, 122 * S], fill=WHITE)  # shoulders

# --- Plus badge -------------------------------------------------------------
bx, by, br = 98 * S, 30 * S, 16 * S
d.ellipse([bx - br, by - br, bx + br, by + br], fill=GREEN)
bar = 3.4 * S   # half thickness of the plus arms
arm = 8.5 * S   # half length of the plus arms
d.rounded_rectangle([bx - arm, by - bar, bx + arm, by + bar], radius=bar, fill=WHITE)
d.rounded_rectangle([bx - bar, by - arm, bx + bar, by + arm], radius=bar, fill=WHITE)

# --- Downscale & save -------------------------------------------------------
out = img.resize((128, 128), Image.LANCZOS)
path = "src/main/resources/assets/altgen/icon.png"
out.save(path, optimize=True)

# --- Sanity checks ----------------------------------------------------------
px = out.load()
assert px[4, 4][3] == 0, "corner should be transparent"
assert px[64, 50][:3] == (255, 255, 255), "head center should be white"
assert out.size == (128, 128)

def near(a, b, tol=12):
    return all(abs(a[i] - b[i]) <= tol for i in range(3))

assert near(px[64, 50], (255, 255, 255)), "head center should be white"
assert near(px[92, 18], GREEN), "plus badge ring should be green"
assert near(px[64, 124], BOTTOM), "bottom badge edge should be gradient bottom color"
print(f"OK: wrote {path} size={out.size} mode={out.mode}")
